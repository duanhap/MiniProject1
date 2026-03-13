package com.example.miniproject1.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.miniproject1.R;
import com.example.miniproject1.model.Room;

import java.util.ArrayList;
import java.util.List;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.RoomViewHolder> {

    private List<Room> roomListAll;
    private final List<Room> roomListFiltered;
    private final OnRoomActionListener listener;
    private String currentQuery = "";

    public RoomAdapter(List<Room> roomList, OnRoomActionListener listener) {
        this.roomListAll = new ArrayList<>(roomList);
        this.roomListFiltered = new ArrayList<>(roomList);
        this.listener = listener;
    }

    public void updateData(List<Room> newList) {
        this.roomListAll = new ArrayList<>(newList);
        filter(currentQuery);
    }

    public void filter(String query) {
        currentQuery = (query == null) ? "" : query;
        roomListFiltered.clear();

        if (currentQuery.trim().isEmpty()) {
            roomListFiltered.addAll(roomListAll);
        } else {
            String lowerQuery = currentQuery.toLowerCase().trim();
            for (Room room : roomListAll) {
                if (room.getRoomName().toLowerCase().contains(lowerQuery)
                        || room.getRoomId().toLowerCase().contains(lowerQuery)
                        || room.getTenantName().toLowerCase().contains(lowerQuery)) {
                    roomListFiltered.add(room);
                }
            }
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_room, parent, false);
        return new RoomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomViewHolder holder, int position) {
        Room room = roomListFiltered.get(position);

        holder.tvRoomName.setText(room.getRoomName());
        holder.tvPrice.setText(String.valueOf(room.getPrice()));
        holder.tvStatus.setText(room.getStatus());

        String status = room.getStatus();
        if ("Còn trống".equals(status)) {
            holder.tvStatus.setTextColor(Color.parseColor("#2E7D32"));
        } else if ("Đã thuê".equals(status)) {
            holder.tvStatus.setTextColor(Color.parseColor("#C62828"));
        } else {
            holder.tvStatus.setTextColor(Color.BLACK);
        }

        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeleteClick(room);
            }
        });

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(room);
            }
        });
    }

    @Override
    public int getItemCount() {
        return roomListFiltered.size();
    }

    public static class RoomViewHolder extends RecyclerView.ViewHolder {
        TextView tvRoomName;
        TextView tvPrice;
        TextView tvStatus;
        Button btnDelete;

        public RoomViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRoomName = itemView.findViewById(R.id.tvRoomName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }

    public interface OnRoomActionListener {
        void onDeleteClick(Room room);

        void onItemClick(Room room);
    }
}
