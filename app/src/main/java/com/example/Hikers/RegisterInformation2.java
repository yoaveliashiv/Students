package com.example.Hikers;

import androidx.annotation.NonNull;

import com.example.students.CardTutor;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

@IgnoreExtraProperties
public class RegisterInformation2 {
    private String name = "";
    private String email = "";
    private String password = "";
    private String idFirebase = "";
    private  String deviceToken = "";

    private int id = -1;
    private String imageUrl = "";

    private List<String> hitchhikingGroups = new ArrayList<>();

    public RegisterInformation2() {
        ;
    }


    public RegisterInformation2(String email, String password, int id) {
        this.email = email;
        this.password = password;
        this.id = id;
    }

    public void removeGroup(int i) {
        hitchhikingGroups.remove(i);

    }


    public void addGroup(String group) {
        this.hitchhikingGroups.add(group);
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public List<String> getHitchhikingGroups() {
        return hitchhikingGroups;
    }

    public void setHitchhikingGroups(List<String> hitchhikingGroups) {
        this.hitchhikingGroups = hitchhikingGroups;
    }

    public int getId() {
        return id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdFirebase() {
        return idFirebase;
    }

    public void setIdFirebase(String idFirebase) {
        this.idFirebase = idFirebase;
    }

    public void foundId() {
        DatabaseReference cardRef = FirebaseDatabase.getInstance().getReference("id");

        cardRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                id = snapshot.getValue(Integer.class);
                id++;
                DatabaseReference cardRef2 = FirebaseDatabase.getInstance().getReference("id");
                cardRef2.setValue(id);
                setId(id);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }
}
