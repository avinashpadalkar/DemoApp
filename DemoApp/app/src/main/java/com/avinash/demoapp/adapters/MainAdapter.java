package com.avinash.demoapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.avinash.demoapp.R;
import com.avinash.demoapp.models.ContactsModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by avinash.padalkar on 25/07/2017.
 */

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    private Context mContext;
    private LayoutInflater inflater;
    private List<ContactsModel> gsonResponseList;
    private List<ContactsModel.Contacts> list = new ArrayList<>();

    public MainAdapter(Context mContext, List<ContactsModel.Contacts> list) {
        this.mContext = mContext;
        this.list = list;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public MainAdapter() {

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = null;
        view = inflater.inflate(R.layout.row_item, parent, false);
        MainAdapter.ViewHolder holder = new MainAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.tvName.setText(list.get(position).getName());
        holder.tvId.setText(list.get(position).getId());
        holder.tvGender.setText(list.get(position).getGender());
        holder.tvAddress.setText(list.get(position).getAddress());
        holder.tvEmail.setText(list.get(position).getEmail());
        holder.tvHome.setText(list.get(position).getPhone().getHome());
        holder.tvOffice.setText(list.get(position).getPhone().getOffice());
        holder.tvMobile.setText(list.get(position).getPhone().getMobile());

        Linkify.addLinks(holder.tvEmail, Linkify.EMAIL_ADDRESSES);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvId, tvEmail, tvAddress, tvGender, tvMobile, tvOffice, tvHome;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvId = (TextView) itemView.findViewById(R.id.tv_id);
            tvEmail = (TextView) itemView.findViewById(R.id.tv_email);
            tvGender = (TextView) itemView.findViewById(R.id.tv_gender);
            tvAddress = (TextView) itemView.findViewById(R.id.tv_address);
            tvHome = (TextView) itemView.findViewById(R.id.tv_home);
            tvOffice = (TextView) itemView.findViewById(R.id.tv_office);
            tvMobile = (TextView) itemView.findViewById(R.id.tv_mobile);
        }
    }
}
