package org.payd.services.interfaces;

import org.payd.data.models.User;

public interface AccountService {

    User createWalletAndBalance(User user) throws Exception;
}
