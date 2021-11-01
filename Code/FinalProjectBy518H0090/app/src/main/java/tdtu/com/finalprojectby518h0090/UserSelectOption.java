package tdtu.com.finalprojectby518h0090;

import tdtu.com.finalprojectby518h0090.model.User;

public interface UserSelectOption {
    void onClickEditEmail(int position);
    void onClickEditPassword(User user);
    void onClickDelete(User user);
    void onChangePermission(User user);
}
