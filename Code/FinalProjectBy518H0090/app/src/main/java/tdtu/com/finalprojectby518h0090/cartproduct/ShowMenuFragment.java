package tdtu.com.finalprojectby518h0090.cartproduct;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
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
    SearchView searchView;

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
        IntoCartInfo();

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
            Toast.makeText(getActivity(), "Thi???u Table Name", Toast.LENGTH_SHORT).show();
        } else {
            //L???y t???ng qu??t v??? tr?? menu
            DatabaseReference drinkCart = FirebaseDatabase.getInstance().getReference("cart").child(tableName);
            //L???y t???ng ph???n t??? ki???m tra c?? hay kh??ng
            drinkCart.child(menuDrink.getMenuDrinkKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    //N???u c?? th?? ch??? c???n c???p nh???t
                    if (snapshot.exists()) {
                        CartDrink cartDrink = snapshot.getValue(CartDrink.class);
                        cartDrink.setQuantity(cartDrink.getQuantity() + 1);
                        cartDrink.setTotalPrice(cartDrink.getQuantity() * cartDrink.getPriceDrink());

                        Map<String, Object> result = new HashMap<>();
                        result.put("quantity", cartDrink.getQuantity());
                        result.put("totalPrice", cartDrink.getTotalPrice());

                        drinkCart.child(menuDrink.getMenuDrinkKey()).updateChildren(result);
                    }
                    //Ch??a c?? th?? tr???c ti???p th??m v??o
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
                    Toast.makeText(getActivity(), "Th??m V??o Card Th???t B???i", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    //L???y List Trong Cart v?? ????a d??? li???u v??o Interface
    private void countNumberBadge() {
        String tableName = textTableName.getText().toString();
        if (tableName.isEmpty()) {
            Toast.makeText(getActivity(), "Thi???u Table Name", Toast.LENGTH_SHORT).show();
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

    private void IntoCartInfo() {
        frame_menu_shop_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                String tableName = textTableName.getText().toString();
                if (tableName.isEmpty()) {
                    tableName = "Default Table";
                } else {
                    tableName = textTableName.getText().toString();
                }

                Bundle bundle = new Bundle();
                bundle.putString("cart_table_name", tableName);

                ShowCartFragment showCartFragment = new ShowCartFragment();
                showCartFragment.setArguments(bundle);

                fragmentTransaction.replace(R.id.replace_show_everything, showCartFragment);
                fragmentTransaction.addToBackStack(ShowCartFragment.class.getName());
                fragmentTransaction.commit();
            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_search, menu);

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
    }
}