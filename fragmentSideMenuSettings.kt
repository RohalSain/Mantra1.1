package com.developer.rohal.mantra


import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_side_menu_settings.*


/**
 * A simple [Fragment] subclass.
 */
class fragmentSideMenuSettings : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_side_menu_settings, container, false)
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        SettingBackButton.setOnClickListener {
            activity?.onBackPressed()
        }
        DailyNotification.setOnClickListener {
        }

        AccountChangePasswordInner.setOnClickListener {
            Log.d("String","yes")
            val transaction = fragmentManager?.beginTransaction()
            transaction?.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
            var fragmentLogin = fragmentForgortPasswordVerify()
            transaction?.replace(R.id.container, fragmentLogin)
            transaction?.addToBackStack("Dashboard Page")
            transaction?.commit()
        }
        AccountSettings.setOnClickListener {
            val transaction = fragmentManager?.beginTransaction()
            //transaction?.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
            var fragmentLogin = FragmentAccountSetting()
            transaction?.replace(R.id.container, fragmentLogin)
            transaction?.addToBackStack("Dashboard Page")
            transaction?.commit()
        }
        Accountfeedbacks.setOnClickListener {
                Log.d("String","yes")
                val transaction = fragmentManager?.beginTransaction()
                //transaction?.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                var fragmentLogin = fragmentFeedback()
                transaction?.replace(R.id.container, fragmentLogin)
                transaction?.addToBackStack("Dashboard Page")
                transaction?.commit()
            }
    }
}// Required empty public constructor
