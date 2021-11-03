package tdtu.com.testproductcart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import tdtu.com.testproductcart.adapter.CartAdapter;
import tdtu.com.testproductcart.listener.CartCount;
import tdtu.com.testproductcart.model.CartDrink;

public class CartActivity extends AppCompatActivity implements CartCount {

    Button btnBack;
    RecyclerView recycler_cart;
    TextView textTotalPrice;
    List<CartDrink> cartDrinkList;
    CartAdapter adapter;
    int count = 0;
    CartCount cartCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        btnBack = findViewById(R.id.btnBack);
        recycler_cart = findViewById(R.id.recycler_cart);
        textTotalPrice = findViewById(R.id.textTotalPrice);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        getValueFromFirebase();

        cartCount = this;


    }

    private void getValueFromFirebase() {
        cartDrinkList = new ArrayList<>();
        adapter = new CartAdapter(CartActivity.this, cartDrinkList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recycler_cart.setLayoutManager(layoutManager);
        recycler_cart.setAdapter(adapter);
        RecyclerView.ItemDecoration decoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recycler_cart.addItemDecoration(decoration);

        FirebaseDatabase.getInstance().getReference("cart").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (cartDrinkList != null) {
                    cartDrinkList.clear();
                }

                for (DataSnapshot cartSnap : snapshot.getChildren()) {
                    CartDrink cartDrink = cartSnap.getValue(CartDrink.class);
                    cartDrinkList.add(cartDrink);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void countinList(List<CartDrink> cartDrinks) {

    }
}