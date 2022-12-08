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


/**
 * Precision dialog
 *
 * @property listener
 * @constructor Create empty Precision dialog
 */
class PrecisionDialog(private var listener: PrecisionListener): DialogFragment() {
    var selectedPrecision:CellPrecision?=null
    companion object{
        const val TAG="PrecisionDialog"
    }

    /**
     * On create view
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return inflater.inflate(R.layout.precission_dialog,container,false)
    }

    /**
     * On view created
     *
     * @param view
     * @param savedInstanceState
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val spinner=view.findViewById<Spinner>(R.id.precisonSpin)
        val tvWidth=view.findViewById<TextView>(R.id.tvWidth)
        val btnSelect=view.findViewById<TextView>(R.id.btnSelect)
        val btnCancel=view.findViewById<TextView>(R.id.btnCancel)
        val tvHeight=view.findViewById<TextView>(R.id.tvHeight)
        val data=ArrayList<Int>()

        getCellPrecisions().values.distinctBy {
            data.add(it)
        }

        /**
         * sortedData is used to store sorted data of data Object that contains values of [CellPrecision].
         */
        val sortedData=data.sorted()
        val adapter = ArrayAdapter(requireContext(),
            android.R.layout.simple_spinner_dropdown_item,sortedData)
        spinner.adapter=adapter
        spinner.setSelection(8)


        spinner.onItemSelectedListener = object : OnItemSelectedListener {
            /**
             * On item selected
             *
             * @param parentView
             * @param selectedItemView
             * @param position
             * @param id
             */
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
                val dimens= getFormattedCellDimensions(selectedPrecision!!)
                val width=dimens.split("x")[0]
                val height=dimens.split("x")[1]
                tvWidth.text=width
                tvHeight.text=height
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {

            }
        }
        btnCancel.setOnClickListener {
            dismiss()
        }
        btnSelect.setOnClickListener {
            if(selectedPrecision!=null) {
                listener.onPrecisionSelected(selectedPrecision!!)
                dismiss()
            }
        }
    }

    /**
     * [PrecisionListener] is an *Interface* and used as Event Listener for Selection of [CellPrecision].
     *
     * @constructor Create empty Precision listener
     */
    interface PrecisionListener{
        fun onPrecisionSelected(cellPrecision: CellPrecision)
    }
}