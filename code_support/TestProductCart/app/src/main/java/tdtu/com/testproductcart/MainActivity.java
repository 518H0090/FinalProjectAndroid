package tdtu.com.testproductcart;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nex3z.notificationbadge.NotificationBadge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tdtu.com.testproductcart.adapter.MenuDrinkAdapter;
import tdtu.com.testproductcart.listener.AddToCartLis;
import tdtu.com.testproductcart.listener.CartCount;
import tdtu.com.testproductcart.model.CartDrink;
import tdtu.com.testproductcart.model.MenuDrink;

public class MainActivity extends AppCompatActivity implements AddToCartLis, CartCount {

    Button btnChangePage;
    RecyclerView recycler_drink;
    NotificationBadge badge;
    MenuDrinkAdapter adapter;
    List<MenuDrink> list;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FrameLayout cartProduct;

    int countValue = 0;
    CartCount listener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnChangePage = findViewById(R.id.btnChangePage);
        recycler_drink = findViewById(R.id.recycler_drink);
        badge = findViewById(R.id.badge);
        list = new ArrayList<>();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        cartProduct = findViewById(R.id.cartProduct);

        //Chuyển Trang
        cartProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });


        btnChangePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddMenuDrink.class);
                startActivity(intent);
            }
        });

        adapter = new MenuDrinkAdapter(this,list);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recycler_drink.setLayoutManager(layoutManager);
        recycler_drink.setAdapter(adapter);

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(MainActivity.this, DividerItemDecoration.VERTICAL);
        recycler_drink.addItemDecoration(itemDecoration);
        
        addValueinRecyclerView();

        adapter.setCartLis(this);
        countNumberBadge();

        listener = this;
    }

    private void countNumberBadge() {
        List<CartDrink> cartDrinkList = new ArrayList<>();

        databaseReference.child("cart").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (cartDrinkList != null) {
                    cartDrinkList.clear();
                }

                for (DataSnapshot drinkSnapshot : snapshot.getChildren()) {
                    CartDrink cartDrink = drinkSnapshot.getValue(CartDrink.class);
                    cartDrinkList.add(cartDrink);
                }
                listener.countinList(cartDrinkList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void addValueinRecyclerView() {
        databaseReference.child("menu")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot drinkSnapShot : snapshot.getChildren()) {
                            MenuDrink menuDrink = drinkSnapShot.getValue(MenuDrink.class);
                            list.add(menuDrink);
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(MainActivity.this, "Lỗi Khi Lấy Dữ liệu", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void addTocart(MenuDrink menuDrink) {
        DatabaseReference userCart = databaseReference.child("cart");

        userCart.child(menuDrink.getDrinkKey()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Có sẵn rồi thì khi bấm thêm sẽ tự cộng 1 vào
                if (snapshot.exists()) {
                    CartDrink cartDrink = snapshot.getValue(CartDrink.class);
                    //Thêm 1 phần tử khi add to cart có sẵn
                    cartDrink.setQuantity(cartDrink.getQuantity()+1);
                    Map<String , Object> result = new HashMap<>();
                    result.put("quantity", cartDrink.getQuantity());
                    result.put("totalPrice", cartDrink.getQuantity() * cartDrink.getDrinkPrice());

                    //Update Cart
                    userCart.child(menuDrink.getDrinkKey()).updateChildren(result);
                }

                else {
                    //Không có thì trực tiếp thêm vào
                    CartDrink cartDrink = new CartDrink(menuDrink.getDrinkKey()
                            , menuDrink.getDrinkName()
                            , menuDrink.getDrinkPrice()
                            ,1
                    ,menuDrink.getDrinkPrice());

                    userCart.child(menuDrink.getDrinkKey()).setValue(cartDrink);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void countinList(List<CartDrink> cartDrinks) {
        int count = 0;
        for (CartDrink cartDrink : cartDrinks) {
            count += cartDrink.getQuantity();
        }
        badge.setNumber(count);
    }
}