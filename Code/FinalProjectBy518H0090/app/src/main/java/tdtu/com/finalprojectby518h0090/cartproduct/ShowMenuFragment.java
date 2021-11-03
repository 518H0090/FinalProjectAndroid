package tdtu.com.finalprojectby518h0090.cartproduct;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
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

import tdtu.com.finalprojectby518h0090.R;
import tdtu.com.finalprojectby518h0090.cartproduct.adapter.ShowMenuAdapter;
import tdtu.com.finalprojectby518h0090.cartproduct.listener.ICartProduct;
import tdtu.com.finalprojectby518h0090.cartproduct.model.CartDrink;
import tdtu.com.finalprojectby518h0090.model.MenuDrink;

public class ShowMenuFragment extends Fragment implements ICartProduct {

    ImageView btnTurnBackShowTable;
    TextView textTableName;
    FrameLayout frame_menu_shop_cart;
    NotificationBadge badge_menu;
    RecyclerView recycler_menu_show;
    ShowMenuAdapter adapter;
    List<MenuDrink> menuDrinkList;
    ICartProduct iCartProduct = this;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_show_menu, container, false);

        initUI(view);
        turnBackTableShow();
        getTableNAme();
        getMenuDataFirebase();
        adapter.setiCartProduct(iCartProduct);
        countNumberBadge();

        return view;
    }

    private void initUI(View view) {
        btnTurnBackShowTable = view.findViewById(R.id.btnTurnBackShowTable);
        textTableName = view.findViewById(R.id.textTableName);
        frame_menu_shop_cart = view.findViewById(R.id.frame_menu_shop_cart);
        badge_menu = view.findViewById(R.id.badge_menu);
        recycler_menu_show = view.findViewById(R.id.recycler_menu_show);
        menuDrinkList = new ArrayList<>();
        adapter = new ShowMenuAdapter(getActivity(), menuDrinkList);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        recycler_menu_show.setLayoutManager(gridLayoutManager);
        recycler_menu_show.setAdapter(adapter);
    }

    private void turnBackTableShow() {
        btnTurnBackShowTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getParentFragmentManager() != null) {
                    getParentFragmentManager().popBackStack();
                }
            }
        });
    }

    private void getTableNAme() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            String tableName = bundle.getString("location_name");
            textTableName.setText(tableName);
        }
    }

    private void getMenuDataFirebase() {
        FirebaseDatabase.getInstance().getReference("menu").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                MenuDrink menuDrink = snapshot.getValue(MenuDrink.class);
                if (menuDrink != null) {
                    menuDrinkList.add(menuDrink);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void addToCartMenuDrink(MenuDrink menuDrink) {
        String tableName = textTableName.getText().toString();
        if (tableName.isEmpty()) {
            Toast.makeText(getActivity(), "Thiếu Table Name", Toast.LENGTH_SHORT).show();
        } else {
            //Lấy tổng quát vị trí menu
            DatabaseReference drinkCart = FirebaseDatabase.getInstance().getReference("cart").child(tableName);
            //Lấy từng phần tử kiểm tra có hay không
            drinkCart.child(menuDrink.getMenuDrinkKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    //Nếu có thì chỉ cần cập nhật
                    if (snapshot.exists()) {
                        CartDrink cartDrink = snapshot.getValue(CartDrink.class);
                        cartDrink.setQuantity(cartDrink.getQuantity() + 1);
                        cartDrink.setTotalPrice(cartDrink.getQuantity() * cartDrink.getPriceDrink());

                        Map<String, Object> result = new HashMap<>();
                        result.put("quantity", cartDrink.getQuantity());
                        result.put("totalPrice", cartDrink.getTotalPrice());

                        drinkCart.child(menuDrink.getMenuDrinkKey()).updateChildren(result);
                    }
                    //Chưa có thì trực tiếp thêm vào
                    else {
                        CartDrink cartDrink = new CartDrink(menuDrink.getMenuDrinkKey()
                                , menuDrink.getTagDrink()
                                , menuDrink.getNameDrink()
                                , menuDrink.getImageDrink()
                                , menuDrink.getPriceDrink(),
                                1,
                                menuDrink.getPriceDrink());

                        drinkCart.child(menuDrink.getMenuDrinkKey()).setValue(cartDrink);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getActivity(), "Thêm Vào Card Thất Bại", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    //Lấy List Trong Cart và đưa dữ liệu vào Interface
    private void countNumberBadge() {
        String tableName = textTableName.getText().toString();
        if (tableName.isEmpty()) {
            Toast.makeText(getActivity(), "Thiếu Table Name", Toast.LENGTH_SHORT).show();
        } else {
            List<CartDrink> cartDrinkList = new ArrayList<>();
            FirebaseDatabase.getInstance().getReference("cart").child(tableName).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if(cartDrinkList != null) {
                        cartDrinkList.clear();
                    }

                    if(snapshot.exists()) {
                        for (DataSnapshot cartSnap : snapshot.getChildren()) {
                            CartDrink cartDrink = cartSnap.getValue(CartDrink.class);
                            cartDrinkList.add(cartDrink);
                        }
                        iCartProduct.countQuantityListInCart(cartDrinkList);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }

    @Override
    public void countQuantityListInCart(List<CartDrink> cartDrinks) {
        int count = 0;
        for (CartDrink cartDrink : cartDrinks) {
            count += cartDrink.getQuantity();
        }
        badge_menu.setNumber(count);
    }
}