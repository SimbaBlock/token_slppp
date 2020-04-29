package com.xyz.slppp.app.modular.system.service.impl;

import com.xyz.slppp.app.modular.system.dao.TransactionMapper;
import com.xyz.slppp.app.modular.system.model.Transaction;
import com.xyz.slppp.app.modular.system.service.TransactionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Resource
    private TransactionMapper transactionMapper;

    @Override
    public int insertTransaction(Transaction transaction) {
        return transactionMapper.insertTransaction(transaction);
    }

}
