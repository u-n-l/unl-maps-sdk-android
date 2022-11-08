package com.app.unl_map_sdk.views

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import com.app.unl_map_sdk.R
import com.app.unl_map_sdk.data.CellPrecision
import com.app.unl_map_sdk.data.getCellPrecisions
import com.app.unl_map_sdk.data.getFormattedCellDimensions


class PrecisionDialog(var listener: PrecisionListener): DialogFragment() {
    var selectedPrecision:CellPrecision?=null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return inflater.inflate(R.layout.precission_dialog,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var spinner=view.findViewById<Spinner>(R.id.precisonSpin)
        var tvWidth=view.findViewById<TextView>(R.id.tvWidth)
        var btnSelect=view.findViewById<TextView>(R.id.btnSelect)
        var btnCancel=view.findViewById<TextView>(R.id.btnCancel)
        var tvHeight=view.findViewById<TextView>(R.id.tvHeight)
        var data=ArrayList<Int>()
        getCellPrecisions().values.distinctBy {
            data.add(it)
        }
        var sortedData=data.sorted()
        val adapter = ArrayAdapter(requireContext(),
            android.R.layout.simple_spinner_dropdown_item,sortedData)
        spinner.adapter=adapter
        spinner.setSelection(8)
        spinner.onItemSelectedListener = object : OnItemSelectedListener {
            @RequiresApi(Build.VERSION_CODES.N)
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View,
                position: Int,
                id: Long,
            ) {

                getCellPrecisions().forEach { (key, value) ->
                    if (value==sortedData[position]) {
                        selectedPrecision=key
                    }
                }
                var dimens= getFormattedCellDimensions(selectedPrecision!!)
                var width=dimens.split("x")[0]
                var height=dimens.split("x")[1]
                tvWidth.text=width
                tvHeight.text=height
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {

            }
        }
        btnCancel.setOnClickListener {
//            listener.onPrecisionCanceled()
            dismiss()
        }
        btnSelect.setOnClickListener {
            if(selectedPrecision!=null){
                listener.onPrecisionSelected(selectedPrecision!!)
                dismiss()
            }
        }
    }

    interface PrecisionListener{
        abstract fun onPrecisionSelected(cellPrecision: CellPrecision)
        abstract fun onPrecisionCanceled()
    }
}