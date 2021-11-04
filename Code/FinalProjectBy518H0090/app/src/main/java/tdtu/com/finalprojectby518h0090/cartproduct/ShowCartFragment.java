package tdtu.com.finalprojectby518h0090.cartproduct;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import tdtu.com.finalprojectby518h0090.R;
import tdtu.com.finalprojectby518h0090.cartproduct.adapter.ShowCartAdapter;
import tdtu.com.finalprojectby518h0090.cartproduct.listener.ICartOption;
import tdtu.com.finalprojectby518h0090.cartproduct.listener.ICartProduct;
import tdtu.com.finalprojectby518h0090.cartproduct.listener.ICartSumTotalPrice;
import tdtu.com.finalprojectby518h0090.cartproduct.model.CartDrink;

public class ShowCartFragment extends Fragment implements ICartOption, ICartSumTotalPrice {

    ImageView btnTurnBackMenuShow;
    TextView textTableNameCart;
    RecyclerView recycler_cart_show;
    List<CartDrink> cartDrinkList;
    ShowCartAdapter adapter;
    ICartOption iCartOption = this;
    TextView text_total_price;

    //
    ICartSumTotalPrice iCartSumTotalPrice = this;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_show_cart, container, false);
        
        initUI(view);
        BackMenuShow();
        getTableName();
        getCartDrinkFromFirebase();
        adapter.setiCartOption(iCartOption);

        //
        getToSumFirebase();

        return view;
    }



    private void initUI(View view) {
        btnTurnBackMenuShow = view.findViewById(R.id.btnTurnBackMenuShow);
        textTableNameCart = view.findViewById(R.id.textTableNameCart);
        recycler_cart_show = view.findViewById(R.id.recycler_cart_show);
        text_total_price = view.findViewById(R.id.text_total_price);

        cartDrinkList = new ArrayList<>();
        adapter = new ShowCartAdapter(getActivity(), cartDrinkList);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recycler_cart_show.setLayoutManager(layoutManager);
        recycler_cart_show.setAdapter(adapter);

    }

    private void BackMenuShow() {
        btnTurnBackMenuShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getParentFragmentManager() != null) {
                    getParentFragmentManager().popBackStack();
                }
            }
        });
    }

    private void getTableName() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            String tableName = bundle.getString("cart_table_name");
            textTableNameCart.setText(tableName);
        }
    }

    private void getCartDrinkFromFirebase() {
        String tableName = textTableNameCart.getText().toString();
        FirebaseDatabase.getInstance().getReference("cart").child(tableName).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                CartDrink cartDrink = snapshot.getValue(CartDrink.class);
                if (cartDrink != null) {
                    cartDrinkList.add(cartDrink);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                CartDrink cartDrink = snapshot.getValue(CartDrink.class);
                if (cartDrink == null || cartDrinkList.isEmpty() || adapter == null) {
                    return;
                }

                for (int i = 0; i < cartDrinkList.size() ; i++) {
                    if (cartDrink.getMenuDrinkKey() == cartDrinkList.get(i).getMenuDrinkKey()) {
                        cartDrinkList.set(i , cartDrink);
                        break;
                    }
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                CartDrink cartDrink = snapshot.getValue(CartDrink.class);
                if (cartDrink == null || cartDrinkList.isEmpty() || adapter == null) {
                    return;
                }

                for (int i = 0; i < cartDrinkList.size() ; i++) {
                    if (cartDrink.getMenuDrinkKey() == cartDrinkList.get(i).getMenuDrinkKey()) {
                        cartDrinkList.remove(cartDrinkList.get(i));
                        break;
                    }
                }

                adapter.notifyDataSetChanged();
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
    public void onClickPlusItem(CartDrink cartDrink) {
        String tableName = textTableNameCart.getText().toString();

        cartDrink.setQuantity(cartDrink.getQuantity()+1);
        cartDrink.setTotalPrice(cartDrink.getQuantity() * cartDrink.getPriceDrink());

        FirebaseDatabase.getInstance().getReference("cart").child(tableName).child(cartDrink.getMenuDrinkKey()).setValue(cartDrink);
    }

    @Override
    public void onClickSubtractItem(CartDrink cartDrink) {
        String tableName = textTableNameCart.getText().toString();

        if (cartDrink.getQuantity() > 1) {
            cartDrink.setQuantity(cartDrink.getQuantity()-1);
            cartDrink.setTotalPrice(cartDrink.getQuantity() * cartDrink.getPriceDrink());
        }

        FirebaseDatabase.getInstance().getReference("cart").child(tableName).child(cartDrink.getMenuDrinkKey()).setValue(cartDrink);
    }

    @Override
    public void onClickDeleteItem(CartDrink cartDrink) {
        String tableName = textTableNameCart.getText().toString();

        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle("Xóa Khỏi Cart");
        dialog.setMessage("Xác Nhận Xóa ?");
        dialog.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseDatabase.getInstance().getReference("cart").child(tableName).child(cartDrink.getMenuDrinkKey()).removeValue();
            }
        });

        dialog.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void getToSumFirebase() {
        String tableName = textTableNameCart.getText().toString();
        FirebaseDatabase.getInstance().getReference("cart").child(tableName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<CartDrink> getListCart = new ArrayList<>();

                if (getListCart != null) {
                    getListCart.clear();
                }

                for (DataSnapshot cartNewSnap : snapshot.getChildren()) {
                    CartDrink cartDrink = cartNewSnap.getValue(CartDrink.class);
                    getListCart.add(cartDrink);
                }
                iCartSumTotalPrice.getListToSum(getListCart);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void getListToSum(List<CartDrink> cartDrinkList) {
        int sum = 0;
        for (CartDrink cartDrink : cartDrinkList) {
            sum += cartDrink.getTotalPrice();
        }
        text_total_price.setText(String.valueOf(sum));
    }
}