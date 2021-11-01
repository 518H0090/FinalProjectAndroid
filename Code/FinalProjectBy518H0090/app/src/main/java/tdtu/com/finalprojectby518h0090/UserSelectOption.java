package tdtu.com.finalprojectby518h0090;

import tdtu.com.finalprojectby518h0090.model.User;

public interface UserSelectOption {
    void onClickEditEmail(User user);
    void onClickEditPassword(User user);
    void onClickDelete(User user);
}
