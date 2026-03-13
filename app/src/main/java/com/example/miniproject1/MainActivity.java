package com.example.miniproject1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.miniproject1.adapter.RoomAdapter;
import com.example.miniproject1.model.Room;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RoomAdapter.OnRoomActionListener {

    private final List<Room> roomList = new ArrayList<>();
    private RoomAdapter roomAdapter;
    private ActivityResultLauncher<Intent> addEditRoomLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initResultLauncher();
        initRecyclerView();
        initFab();
        seedInitialData();
    }

    private void initResultLauncher() {
        addEditRoomLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() != RESULT_OK || result.getData() == null) {
                        return;
                    }

                    Intent data = result.getData();
                    Room room = (Room) data.getSerializableExtra(AddEditRoomActivity.EXTRA_ROOM);
                    int position = data.getIntExtra(AddEditRoomActivity.EXTRA_POSITION, -1);
                    if (room == null) {
                        return;
                    }

                    if (position >= 0 && position < roomList.size()) {
                        roomList.set(position, room);
                        roomAdapter.notifyDataSetChanged();
                        Toast.makeText(this, "Đã cập nhật phòng", Toast.LENGTH_SHORT).show();
                    } else {
                        roomList.add(room);
                        roomAdapter.notifyItemInserted(roomList.size() - 1);
                        Toast.makeText(this, "Đã thêm phòng", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    private void initRecyclerView() {
        RecyclerView recyclerViewRooms = findViewById(R.id.recyclerViewRooms);
        recyclerViewRooms.setLayoutManager(new LinearLayoutManager(this));
        roomAdapter = new RoomAdapter(roomList, this);
        recyclerViewRooms.setAdapter(roomAdapter);
    }

    private void initFab() {
        FloatingActionButton fabAddRoom = findViewById(R.id.fabAddRoom);
        fabAddRoom.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddEditRoomActivity.class);
            addEditRoomLauncher.launch(intent);
        });
    }

    private void seedInitialData() {
        roomList.add(new Room("P101", "Phòng 101", 2500000, "Còn trống", "", ""));
        roomList.add(new Room("P102", "Phòng 102", 2800000, "Đã thuê", "Nguyễn Văn A", "0909000001"));
        roomList.add(new Room("P103", "Phòng 103", 3000000, "Còn trống", "", ""));
        roomAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDeleteClick(int position) {
        if (position < 0 || position >= roomList.size()) {
            return;
        }

        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc muốn xóa phòng này?")
                .setPositiveButton("YES", (dialog, which) -> {
                    roomList.remove(position);
                    roomAdapter.notifyItemRemoved(position);
                    Toast.makeText(MainActivity.this, "Đã xóa phòng", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("NO", null)
                .show();
    }

    @Override
    public void onItemClick(int position) {
        if (position < 0 || position >= roomList.size()) {
            return;
        }

        Intent intent = new Intent(this, AddEditRoomActivity.class);
        intent.putExtra(AddEditRoomActivity.EXTRA_ROOM, roomList.get(position));
        intent.putExtra(AddEditRoomActivity.EXTRA_POSITION, position);
        addEditRoomLauncher.launch(intent);
    }
}