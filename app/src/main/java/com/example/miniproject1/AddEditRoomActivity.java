package com.example.miniproject1;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.miniproject1.model.Room;
import com.google.android.material.appbar.MaterialToolbar;

public class AddEditRoomActivity extends AppCompatActivity {

    public static final String EXTRA_ROOM = "extra_room";
    public static final String EXTRA_POSITION = "extra_position";

    private EditText edtRoomId;
    private EditText edtRoomName;
    private EditText edtPrice;
    private Spinner spinnerStatus;
    private EditText edtTenantName;
    private EditText edtPhone;
    private int editPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_room);

        initViews();
        setupToolbar();
        setupStatusSpinner();
        bindEditDataIfAny();
        setupSaveButton();
    }

    private void initViews() {
        edtRoomId = findViewById(R.id.edtRoomId);
        edtRoomName = findViewById(R.id.edtRoomName);
        edtPrice = findViewById(R.id.edtPrice);
        spinnerStatus = findViewById(R.id.spinnerStatus);
        edtTenantName = findViewById(R.id.edtTenantName);
        edtPhone = findViewById(R.id.edtPhone);
    }

    private void setupToolbar() {
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void setupStatusSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.room_status_array,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatus.setAdapter(adapter);
    }

    private void bindEditDataIfAny() {
        Intent intent = getIntent();
        Room room = (Room) intent.getSerializableExtra(EXTRA_ROOM);
        editPosition = intent.getIntExtra(EXTRA_POSITION, -1);

        if (room == null) {
            return;
        }

        edtRoomId.setText(room.getRoomId());
        edtRoomName.setText(room.getRoomName());
        edtPrice.setText(String.valueOf(room.getPrice()));
        edtTenantName.setText(room.getTenantName());
        edtPhone.setText(room.getPhone());

        String[] statuses = getResources().getStringArray(R.array.room_status_array);
        for (int i = 0; i < statuses.length; i++) {
            if (statuses[i].equals(room.getStatus())) {
                spinnerStatus.setSelection(i);
                break;
            }
        }
    }

    private void setupSaveButton() {
        Button btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(v -> saveRoom());
    }

    private void saveRoom() {
        String roomId = edtRoomId.getText().toString().trim();
        String roomName = edtRoomName.getText().toString().trim();
        String priceText = edtPrice.getText().toString().trim();
        String status = spinnerStatus.getSelectedItem().toString();
        String tenantName = edtTenantName.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();

        if (TextUtils.isEmpty(roomId) || TextUtils.isEmpty(roomName) || TextUtils.isEmpty(priceText)) {
            Toast.makeText(this, "Vui lòng nhập roomId, roomName và price", Toast.LENGTH_SHORT).show();
            return;
        }

        double price;
        try {
            price = Double.parseDouble(priceText);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Price không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        Room room = new Room(roomId, roomName, price, status, tenantName, phone);

        Intent resultIntent = new Intent();
        resultIntent.putExtra(EXTRA_ROOM, room);
        resultIntent.putExtra(EXTRA_POSITION, editPosition);
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}
