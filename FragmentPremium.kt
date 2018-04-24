package com.developer.rohal.mantra


import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.jjoe64.graphview.DefaultLabelFormatter
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import kotlinx.android.synthetic.main.fragment_fragment_premium.*
import java.text.NumberFormat
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.data.BarData
import kotlinx.android.synthetic.main.fragment_forgort_password_verify.*
import kotlinx.android.synthetic.main.items.view.*
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.HashMap


class FragmentPremium : Fragment()
{
    //
    var arry:ArrayList<Score>?= ArrayList()
    var monthName = arrayOf("Jan", "Feb", "March", "April", "May", "June", "July", "Aug", "Sept", "Oct", "Nov", "Dec")
    var mMonths = arrayOf(" ","  2 Feb", "3 Feb", "4 Feb", "5 Feb", "6 Feb", "7 Feb", "8 Feb", "9 Feb", "10 Feb")
    var mMonthsd = arrayOf(" ","  2 Feb")

    var dateGraph:ArrayList<String> = ArrayList()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_fragment_premium, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        GraphBackButton.setOnClickListener {
            activity?.onBackPressed()
        }
        loading_indicateGraph.visibility=View.VISIBLE
        val call=ApiCall().retrofitClient()?.create(RedditAPI::class.java)?.getPoints(LoginDataUser.instance.token!!)
        call?.enqueue(object: Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?)
            {
                if(response?.isSuccessful!!)
                {
                    loading_indicateGraph.visibility=View.INVISIBLE
                    val json: String = response?.body()!!.string();
                    Log.d("JSON Graph", "onResponse: json: " + json);
                    val data: JSONObject?=JSONObject(json)
                    var status = data?.get("success")
                    if(data?.get("success")==1 &&data?.get("message").equals("Request successful"))
                    {

                        Log.d("status", "yes")
                        val arrayJson = data.getJSONArray("data")
                        for (i in 0 until arrayJson.length()) {
                            val actor = arrayJson.getJSONObject(i)
                            val Date = actor.getString("createdAt")
                            val score = actor.get("score").toString().toInt()
                                arry?.add(Score(score,Helper.instance.getDate(Date)))
                        }
                        Log.d("ArraySize","${arry?.size}")
                        val yValues: ArrayList<Entry> = ArrayList()
                        yValues.add(Entry(0f, 0F, "Label 1"))
                        dateGraph.add(" ")
                        for(i in 0 ..arry?.size!!-1)
                        {
                           val temp=(i+1).toFloat()
                            yValues.add(Entry(temp,arry!!.get(i).score.toFloat()))
                            Log.d("Data",arry!!.get(i).score.toString())
                            dateGraph.add(arry!!.get(i).createdDate)
                        }
                        var set1: LineDataSet = LineDataSet(yValues," ")
                        set1.valueTypeface = Typeface.createFromAsset(context?.assets, "fonts/BubblegumSans-Regular.ttf")
                        set1.fillAlpha = 110
                        set1.fillAlpha= 200
                        val leftAxis = chart1.axisLeft
                        leftAxis.setDrawGridLines(false)
                        leftAxis.setAxisMinimum(0f)
                        val xAxis = chart1.getXAxis()
                        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM)
                        xAxis.setAxisMinimum(0f)
                        xAxis.setGranularity(1f)
                        xAxis.setValueFormatter(IAxisValueFormatter { value, axis -> dateGraph[value.toInt() %7] })
                        xAxis.setTypeface(Typeface.createFromAsset(context?.assets, "fonts/BubblegumSans-Regular.ttf"))
                        // set1.setFillColor(Color.RED);

                        // set the line to be drawn like this "- - - - - -"
                        // set1.enableDashedLine(10f, 5f, 0f);
                        // set1.enableDashedHighlightLine(10f, 5f, 0f);

                        //setting the custom font stlye to y axis
                        var yAxis:YAxis= chart1.getAxisLeft();
                        yAxis.setTypeface(Typeface.createFromAsset(context?.assets, "fonts/BubblegumSans-Regular.ttf"))


                        set1.setColor(Color.BLACK);
                        set1.setCircleColor(Color.WHITE);
                        set1.setLineWidth(1f);
                        set1.setCircleRadius(3f);
                        set1.setDrawCircleHole(true);
                        set1.setValueTextSize(9f);
                        set1.setDrawFilled(true);
                        val dataSets: ArrayList<ILineDataSet> = ArrayList()
                        dataSets.add(set1)
                        val data: LineData = LineData(dataSets)
                        chart1.getAxisLeft().setDrawGridLines(true);
                        chart1.getXAxis().setDrawGridLines(false);
                        chart1.getDescription().setEnabled(false);
                        val rightYAxis = chart1.getAxisRight()
                        rightYAxis.setEnabled(false)
                        val rightAxis = chart1.axisLeft
                        rightAxis.setDrawGridLines(true)
                        chart1.data = data
                        chart1.notifyDataSetChanged(); // let the chart know it's data changed
                        chart1.invalidate()
                        }
                    else{
                        var view=ReuseMethod()
                    }
                }
            }

            override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {

            }

        })


    }
}