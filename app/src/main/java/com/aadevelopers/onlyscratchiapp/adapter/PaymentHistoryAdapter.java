package com.aadevelopers.onlyscratchiapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aadevelopers.onlyscratchiapp.R;
import com.aadevelopers.onlyscratchiapp.models.PaymentHistoryModel;

import java.util.ArrayList;

public class PaymentHistoryAdapter extends RecyclerView.Adapter<PaymentHistoryAdapter.ViewHolder> {
    private ArrayList<PaymentHistoryModel> paymentHistoryModelArrayList;

    public PaymentHistoryAdapter(ArrayList<PaymentHistoryModel> paymentHistoryModelArrayList) {
        this.paymentHistoryModelArrayList = paymentHistoryModelArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.payment_history_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PaymentHistoryModel model = paymentHistoryModelArrayList.get(position);

        holder.payment_id.setText("Payment Id : " + model.getId());
        holder.redeem_points.setText("Redeem Points : " + model.getRedeem_point());
        holder.payment_mode.setText("Payment Mode : " + model.getPayment_mode());
        holder.payment_information.setText("Payment Information : " + model.getPayment_info());
        holder.payment_date.setText("Payment Time : " + model.getPayment_time());
    }

    @Override
    public int getItemCount() {
        return paymentHistoryModelArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView payment_id, redeem_points, payment_mode, payment_information, payment_date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.payment_id = itemView.findViewById(R.id.payment_id);
            this.redeem_points = itemView.findViewById(R.id.redeem_points);
            this.payment_mode = itemView.findViewById(R.id.payment_mode);
            this.payment_information = itemView.findViewById(R.id.payment_information);
            this.payment_date = itemView.findViewById(R.id.payment_date);
        }
    }
}
