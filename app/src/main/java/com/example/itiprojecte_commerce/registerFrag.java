package com.example.itiprojecte_commerce;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;


public class registerFrag extends Fragment {



    public registerFrag() {
        // Required empty public constructor
    }

EditText name_register,phone_register,password_register;
    Button register_btn;
    ProgressDialog progressDialog;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



       View v =  inflater.inflate(R.layout.fragment_register, container, false);

       register_btn = v.findViewById(R.id.register_btn);
        name_register = v.findViewById(R.id.name_register);
        phone_register = v.findViewById(R.id.phone_register);
        password_register = v.findViewById(R.id.password_register);
        progressDialog = new ProgressDialog(getContext());




        return v;
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        final NavController navController = Navigation.findNavController(v);
        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String name = name_register.getText().toString();
                final String phone = phone_register.getText().toString();
                final String password = password_register.getText().toString();

                if(name.isEmpty() ||phone.isEmpty()||password.isEmpty()){

                    Toast.makeText(getContext(), "please enter all fields", Toast.LENGTH_SHORT).show();
                }
                else{

                    progressDialog.setTitle("Create Account In Progress");
                    progressDialog.setMessage("waiting");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();




                    final DatabaseReference databaseReference;
                    databaseReference = FirebaseDatabase.getInstance().getReference();
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                        {




                            if (!(dataSnapshot.child("Users").child(phone).exists()))
                            {
                                HashMap<String, Object> userdataMap = new HashMap<>();
                                userdataMap.put("phone", phone);
                                userdataMap.put("password", password);
                                userdataMap.put("name", name);



                                databaseReference.child("Users").child(phone).updateChildren(userdataMap)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task)
                                            {

                                                if (task.isSuccessful())
                                                {
                                                    Toast.makeText(getContext(), "Congratulations, your account has been created.", Toast.LENGTH_SHORT).show();
                                                    progressDialog.dismiss();

                                                    navController.navigate(R.id.action_registerFrag_to_loginFrag);                                    }
                                                else
                                                {
                                                    progressDialog.dismiss();
                                                    Toast.makeText(getContext(), "Network Error: Please try again after some time...", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }
                            else
                            {
                                Toast.makeText(getContext(), "This " + phone + " already exists.", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                                Toast.makeText(getContext(), "Please try again using another phone number.", Toast.LENGTH_SHORT).show();

                                navController.navigate(R.id.action_registerFrag_to_main_frag);
                            }
                        }




                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });



                }

            }
        });
    }



    private void validatePhoneNumber(final String name, final String phone, final String password) {



    }


}