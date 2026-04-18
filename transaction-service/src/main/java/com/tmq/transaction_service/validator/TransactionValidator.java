package com.tmq.transaction_service.validator;

import com.tmq.transaction_service.entity.WalletEntity;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class TransactionValidator {
    public boolean isWalletBalanceAmountGreaterThanRequest(WalletEntity wallet,
                                                            BigDecimal request){
        if (wallet.getBalance().subtract(request).compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("Insufficient balance");
        }
        return true;
    }
}
