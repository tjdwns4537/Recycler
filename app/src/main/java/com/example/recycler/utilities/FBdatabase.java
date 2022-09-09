package com.example.recycler.utilities;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FBdatabase {
    public static FirebaseDatabase database = FirebaseDatabase.getInstance();
    public static final DatabaseReference boardRef = database.getReference("board");
    public static String Imagepath;
}
