package com.developer.rohal.mantra


import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.*
import android.widget.AbsListView
import android.widget.Toast
import com.github.florent37.singledateandtimepicker.SingleDateAndTimePicker
import kotlinx.android.synthetic.main.fragment_adpater_setting.view.*
import kotlinx.android.synthetic.main.fragment_alarm.view.*
import kotlinx.android.synthetic.main.fragment_fragment_dashboard.*
import kotlinx.android.synthetic.main.fragment_fragment_dashboard.view.*
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

class FragmentDashboard : Fragment()
{
    var listOfQuote: ArrayList<PojoAllQuoteDetailDashBoard> = ArrayList()

    var extraQuote:ArrayList<PojoAllQuoteDetailDashBoard> = ArrayList();
    var currentItem:Int=0
    var totalItem:Int=0
    var Scrollout:Int=0

    var isScroll=false
    var clickedId:String?=null
    var AlarmView: View? = null
    var adapter: DashboardCustomAdapter? = null
    var contextD: Context? = null
    var monthName = arrayOf("Jan", "Feb", "March", "April", "May", "June", "July", "Aug", "Sept", "Oct", "Nov", "Dec")
    var pos: Int? = null
    var mview: View? = null
    var check=false
    var loader = Loader()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        userPic.setImageURI("${Session(context?.applicationContext!!).getPrfofilePic()}?dim=500x500")
        displayName.text = Session(context?.applicationContext!!).getName()
        menu.setOnClickListener {
            mDrawer.openDrawer(Gravity.START)
            progressBar.visibility=View.INVISIBLE
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mview = inflater.inflate(R.layout.fragment_fragment_dashboard, container, false)
        return mview
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        contextD = context
        if(LoginDataUser.instance.listOfQuote.size==0)
        apiCall(mview!!, (ApiCall.instance.retrofitClient()?.create(RedditAPI::class.java))?.GetQuote(LoginDataUser.instance.token!!), "getallquote")
        else
        {
           settingValuesIntoRecyclerView(LoginDataUser.instance.listOfQuote)
        }
        view.home?.setOnClickListener {
            mDrawer.closeDrawer(Gravity.START)
        }
        view.SideReflection?.setOnClickListener {
            mDrawer.closeDrawer(Gravity.START)
            fragmentManager?.beginTransaction()
                    ?.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                    ?.replace(R.id.container, FragmentReflections())
                    ?.addToBackStack("sideReflection")
                    ?.commit()
        }
        view.favourites?.setOnClickListener {
            mDrawer.closeDrawer(Gravity.START)
            fragmentManager?.beginTransaction()
                    ?.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                    ?.replace(R.id.container, FragmentFavourite())
                    ?.addToBackStack("favourite")
                    ?.commit()
        }
        view.MantraMePremium?.setOnClickListener {
            mDrawer.closeDrawer(Gravity.START)
            fragmentManager?.beginTransaction()
                    ?.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                    ?.replace(R.id.container, FragmentPremium())
                    ?.addToBackStack("manraMeMore")
                    ?.commit()

        }
        view.Alarm?.setOnClickListener {
            AlarmView = it
            Alarm_box(it.context)
            mDrawer.closeDrawer(Gravity.START)
        }
        view.ShareThisApp?.setOnClickListener {
            Helper.instance.shareIt(context!!,"Here is the share content body","Subject Here")
            mDrawer.closeDrawer(Gravity.START)
        }
        view.settings?.setOnClickListener {
            mDrawer.closeDrawer(Gravity.START)

            fragmentManager?.beginTransaction()
                    ?.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                    ?.replace(R.id.container, fragmentSideMenuSettings())
                    ?.addToBackStack("dashboard")
                    ?.commit()
        }
        view.logouts?.setOnClickListener {
            mDrawer.closeDrawer(Gravity.START)
            val builder = AlertDialog.Builder(it.context)
            builder.setTitle("Logout")
            builder.setMessage("Are you sure you want to logout?")
            builder.setPositiveButton("YES") { dialog, which ->
                while (fragmentManager!!.backStackEntryCount > 0) {
                   fragmentManager!!.popBackStackImmediate()
                }
                Session(context?.applicationContext!!).setLoginStatus()
                Session(context?.applicationContext!!).setLoggedin(false, " ", " ", " ", " ", " ")
                Session(context?.applicationContext!!).removeAll()

                fragmentManager?.beginTransaction()
                        ?.replace(R.id.container,fragmentLogin())
                        ?.commit()

                dialog.dismiss()
            }

            builder.setNegativeButton("NO") { dialog, which ->
                dialog.dismiss()
            }
            val alert = builder.create()
            alert.show()
        }
    }

    fun Alarm_box(view: Context) {

        var hours: Int? = null
        var minutes: Int? = null
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        val inflater: LayoutInflater = layoutInflater
        val alarmView: View = inflater.inflate(R.layout.fragment_alarm, null)
        builder.setView(alarmView)
        val Dilog: Dialog = builder.create()
        Dilog.show()
        alarmView.DatePickerAlarm.setStepMinutes(1)
        alarmView.DatePickerAlarm.addOnDateChangedListener(SingleDateAndTimePicker.OnDateChangedListener
        { displayed, date ->
            Log.d("Value is ", "${date.hours}   ${date.minutes}")
            if (date.minutes > -1 && date.minutes < 10) {
                alarmView.clockAlarm.text = "${date.hours}:0${date.minutes}"
            } else {
                alarmView.clockAlarm.text = "${date.hours}:${date.minutes}"
            }

            hours = date.hours
            minutes = date.minutes
            Log.d("Date", "${date.date}")
            Log.d("Month", "${date.month}")
            Log.d("Year", "${date.year}")


        }
        )
        alarmView.CancelAlarm.setOnClickListener {
            Dilog.dismiss()
        }
        alarmView.okAlarm.setOnClickListener {
            Toast.makeText(it.context, "Time is ${alarmView.DatePickerAlarm.date.hours}", Toast.LENGTH_SHORT).show()
            Log.d("Data", "Time is ${hours} : ${minutes}")
            val SaveData = com.developer.rohal.mantra.AlarmManager(it.context)
            SaveData.SaveData(hours!!, minutes!!)
            SaveData.SetAlarm()
            val headerMap = HashMap<String, RequestBody>()
            headerMap.put("alarm", RequestBody.create(MediaType.parse("text/plain"), "${hours}:${minutes}"))
            setAlarmAPi("${hours}:${minutes}")
            Dilog.dismiss()
        }
    }

    private fun setAlarmAPi(alarm: String) {
        loading_indicate.visibility = View.VISIBLE
        apiCall(mview!!, (ApiCall.instance.retrofitClient()?.create(RedditAPI::class.java))?.setAlarm(LoginDataUser.instance.token!!,alarm), "setalarm")

    }
    fun customAdapterSetting(view: Context?, item: PojoAllQuoteDetailDashBoard) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        val view2: View = layoutInflater.inflate(R.layout.fragment_adpater_setting, null)
        builder.setView(view2)
        val dialog: Dialog = builder.create()
        dialog.show()

        view2.fav.typeface = Typeface.createFromAsset(context?.assets, "fonts/BubblegumSans-Regular.ttf")
        view2.Ref.typeface = Typeface.createFromAsset(context?.assets, "fonts/BubblegumSans-Regular.ttf")
        view2.shareit.typeface = Typeface.createFromAsset(context?.assets, "fonts/BubblegumSans-Regular.ttf")
        view2.uploadImage.typeface = Typeface.createFromAsset(context?.assets, "fonts/BubblegumSans-Regular.ttf")

        if (item.like == "1") {
            Log.d("like", "yes")
            val imgResource = R.drawable.ic_selected_favorite_white
            view2.fav.setCompoundDrawablesWithIntrinsicBounds(imgResource, 0, 0, 0)
        }


        view2.fav_lay.setOnClickListener{

            apiCall(view2,(ApiCall.instance.retrofitClient()?.create(RedditAPI::class.java))?.GetMark(LoginDataUser.instance.token!!,LoginDataUser.instance.QuoteID),"like")
        }
        view2.Ref_lay.setOnClickListener {
            LoginDataUser.instance.Quotes = LoginDataUser.instance.listOfQuote.get(pos!!).text
            LoginDataUser.instance.BackGround = LoginDataUser.instance.listOfQuote.get(pos!!).background
            LoginDataUser.instance.QuoteID = LoginDataUser.instance.listOfQuote.get(pos!!).id
            dialog.dismiss()
                    (context as FragmentActivity).supportFragmentManager.beginTransaction()
                    ?.replace(R.id.container,FragmentDashboardReflection())
                    ?.addToBackStack("recyclerViewReflection")
                    ?.commit()
        }
        view2.shareIt_lay.setOnClickListener {
            Helper.instance.shareIt(context!!,"Here is the share content body","Subject Here")
        }
        view2.uploadImage_lay.setOnClickListener {
            settingValue()
            dialog.dismiss()

             (context as FragmentActivity).supportFragmentManager.beginTransaction()
                    ?.replace(R.id.container, FragmentMakeImage())
                    ?.addToBackStack("makeImage")
                    ?.commit()
        }
    }
    private fun apiCall(it: View, call: Call<ResponseBody>?, s: String)
    {
        call?.enqueue(object : Callback<ResponseBody>
        {
            override fun onResponse(call: Call<ResponseBody>, response: retrofit2.Response<ResponseBody>?)
            {
                Log.d("Api Dashboard", "Server Response: " + response.toString());
                try {
                    var json: String? = null
                    json = response?.body()?.string()
                    Log.d("Json Object ","${json}")
                    if (json == null)
                    {
                        Log.d("Api Like Hit "," Error Ocuured")
                    }
                    else
                    {
                        if(s.equals("like"))
                        {
                            Log.d("Api(like/dislike)", "Server Response: " + response.toString());
                            if(JSONObject(json).get("success").toString().toInt()==1) {
                                if (JSONObject(json).get("message").toString().equals("Request successful")) {
                                    it.fav.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_selected_favorite_white, 0, 0, 0)
                                } else {
                                    it.fav.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_favorite_grey, 0, 0, 0)
                                }
                            }
                        }
                        else if(s.equals("getallquote"))
                        {
                            Log.d("Api(getallquote)", "Server Response: " + response.toString())
                            if(JSONObject(json).get("success").toString().toInt()==1) {
                                val cast: JSONArray = JSONObject(json).getJSONArray("data")
                                for (i in 0 until cast.length()) {
                                    LoginDataUser.instance.listOfQuote.add(PojoAllQuoteDetailDashBoard(
                                            cast.getJSONObject(i).getString("_id"),
                                            cast.getJSONObject(i).getString("text"),
                                            cast.getJSONObject(i).getString("background"),
                                            cast.getJSONObject(i).getString("favorite"),
                                            getDate(cast.getJSONObject(i).getString("createdAt")),
                                            getDate(cast.getJSONObject(i).getString("createdAt")))
                                    )
                                }
                               settingValuesIntoRecyclerView(LoginDataUser.instance.listOfQuote)
                                loading_indicate.visibility = View.INVISIBLE
                            }

                        }
                        else if(s.equals("setalarm"))
                        {
                            Log.d("Api(setAlarm)", "Server Response: " + response.toString())
                            if(JSONObject(json).get("success").toString().toInt()==1) {
                                loading_indicate.visibility = View.INVISIBLE
                                Log.d("alarm Set","yes Successfully Set ")
                            }

                        }
                        else if(s.equals("nextQuote"))
                        {
                            Log.d("Api(nextQuote)", "Server Response: " + response.toString())
                            if(JSONObject(json).get("success").toString().toInt()==1) {
                                val cast: JSONArray = JSONObject(json).getJSONArray("data")
                                Log.d("Quotes are", cast.toString())
                                if(cast.length()==0)
                                {

                                }
                                else {
                                    extraQuote.clear()
                                    for (i in 0 until cast.length()) {
                                        extraQuote.add(PojoAllQuoteDetailDashBoard(
                                                cast.getJSONObject(i).getString("_id"),
                                                cast.getJSONObject(i).getString("text"),
                                                cast.getJSONObject(i).getString("background"),
                                                cast.getJSONObject(i).getString("favorite"),
                                                getDate(cast.getJSONObject(i).getString("createdAt")),
                                                getDate(cast.getJSONObject(i).getString("createdAt")))
                                        )
                                    }
                                    setData(extraQuote)
                                }
                            }

                        }

                    }


                }
                catch (e: JSONException) {
                    Log.e("Api Dashboard", "Server Response(Json Exception Occur): " + e)
                }
                catch (e: IOException) {
                    Log.e("Api Dashboard", "Server Response(IO Exception Occur): " + e)
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("Api Dashboard", "Server Response(On Failure): " +t)
            }
        })
    }



    private fun settingValue()
    {
        val item: PojoAllQuoteDetailDashBoard = LoginDataUser.instance.listOfQuote.get(pos!!)
        LoginDataUser.instance.Quotes = item.text
        LoginDataUser.instance.BackGround = item.background
        LoginDataUser.instance.QuoteID = item.id
    }
    fun getDate(Date:String):String
    {
        var statusDate: String? = null
        val year = StringBuffer()
        val month = StringBuffer()
        val date = StringBuffer()
        val hours = StringBuffer()
        val min = StringBuffer()

        for (j in 0..3)
            year.append(Date[j])
        for (j in 5..6)
            month.append(Date[j])
        for (j in 8..9)
            date.append(Date[j])
        for (j in 11..12)
            hours.append(Date[j])
        for (j in 14..15)
            min.append(Date[j])
        var temp = month.toString().toInt()
        statusDate = "$date ${monthName[--temp]} $year"
        var tempHours = hours.toString().toInt()
        val tempMinutes = min.toString().toInt()
        if (tempHours == 12 && tempMinutes == 0) {
            Log.d("Time", "Time is ${tempHours}:${tempMinutes} noon")
        } else if (tempHours > 12) {
            tempHours = tempHours - 12
            Log.d("Time", "Time is ${tempHours}:${tempMinutes} PM")
        } else {
            Log.d("Time", "Time is ${tempHours}:${tempMinutes} AM")
        }
        return statusDate
    }

    fun settingValuesIntoRecyclerView(listOfQuote: ArrayList<PojoAllQuoteDetailDashBoard>)
    {
        adapter = DashboardCustomAdapter(listOfQuote, contextD!!, object : DashboardCustomAdapter.OnViewClicked {
            override fun onClick(position: Int) {
                Log.d("Clicked yes ", "yes ${position}")
                LoginDataUser.instance.QuoteID = listOfQuote.get(position).id
                LoginDataUser.instance.chnageBackgroundPos = position
                pos = position
                customAdapterSetting(context, listOfQuote.get(position))
                clickedId= listOfQuote.get(position).id
                if(LoginDataUser.instance.backGroundStatus!=null)
                {
                    adapter?.ChangeItem(position, LoginDataUser.instance.backGroundChange!!)
                    LoginDataUser.instance.backGroundStatus=null
                }
            }
        })
        val manager=GridLayoutManager(context,1)
        recyclerview?.layoutManager = manager
        recyclerview?.adapter = adapter
        refesh(manager,listOfQuote,adapter)

    }

    private fun fetchData(listOfQuote: ArrayList<PojoAllQuoteDetailDashBoard>, adapter: DashboardCustomAdapter?)
    {
        this.listOfQuote=listOfQuote
        this.adapter=adapter
        var item=listOfQuote.get(listOfQuote.size-1)
        apiCall(mview!!, (ApiCall.instance.retrofitClient()?.create(RedditAPI::class.java))?.GetNextQuote(LoginDataUser.instance.token!!,item.id), "nextQuote")

    }
    private fun setData(extraQuote: ArrayList<PojoAllQuoteDetailDashBoard>) {
        if(extraQuote.size!=0) {
            for (i in 0 until extraQuote.size) {
                listOfQuote.add(extraQuote.get(i))
            }
            adapter?.notifyDataSetChanged()
            progressBar.visibility = View.INVISIBLE
        }
        else
        {

        }

    }
    fun refesh(manager: GridLayoutManager, listOfQuote: ArrayList<PojoAllQuoteDetailDashBoard>, adapter: DashboardCustomAdapter?)
    {
        recyclerview.addOnScrollListener(object : RecyclerView.OnScrollListener()
        {
            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
                {
                    isScroll=true
                }
            }

            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                currentItem=manager.childCount
                totalItem=manager.itemCount
                Scrollout=manager.findFirstVisibleItemPosition()
                if(isScroll && (currentItem+Scrollout==totalItem))
                {
                   // progressBar.visibility=View.VISIBLE
                    isScroll=false
                    fetchData(listOfQuote,adapter)
                }
            }
        })
    }
}







