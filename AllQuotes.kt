package com.developer.rohal.mantra

class AllQuotes
{
    var data: ArrayList<Quote> = ArrayList<Quote>()
    private object Holder {
        val INSTANCE = AllQuotes()
    }
    companion object {
        val instance:AllQuotes by lazy { AllQuotes.Holder.INSTANCE }
    }
    data class Quote(var id: String, var text:String, var background:String, var like: String, var Date:String, var time:String)
}
