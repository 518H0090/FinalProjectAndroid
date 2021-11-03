package tdtu.com.testproductcart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import tdtu.com.testproductcart.model.MenuDrink;

public class AddMenuDrink extends AppCompatActivity {

    EditText editName, editPrice;
    Button btnThem, btnHuy;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_menu_drink);

        editName = findViewById(R.id.editName);
        editPrice = findViewById(R.id.editPrice);
        btnThem = findViewById(R.id.btnThem);
        btnHuy = findViewById(R.id.btnHuy);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddMenuDrink.this, MainActivity.class);
                startActivity(intent);
            }
        });

        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameDrink = editName.getText().toString();
                String priceDrink = editPrice.getText().toString();

                if (nameDrink.isEmpty() || priceDrink.isEmpty()) {
                    Toast.makeText(AddMenuDrink.this, "Thiếu", Toast.LENGTH_SHORT).show();
                } else {
                    String DrinkKey = databaseReference.push().getKey();
                    int price = Integer.parseInt(priceDrink);

                    MenuDrink menuDrink = new MenuDrink(DrinkKey, nameDrink, price);
                    databaseReference.child("menu").child(DrinkKey).setValue(menuDrink)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(AddMenuDrink.this, "Thêm Thành Công", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });

    }
}