package com.supplychain.blockchain_tracker.blockchain;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class Blockchain {

    private final List<Block> chain = new ArrayList<>();

    public Blockchain() {
        chain.add(createGenesisBlock());
    }

    private Block createGenesisBlock() {
        return new Block(0, "Genesis Block", "0");
    }

    public Block addBlock(String data) {
        Block lastBlock = getLastBlock();
        Block newBlock = new Block(chain.size(), data, lastBlock.getHash());
        chain.add(newBlock);
        return newBlock;
    }

    public boolean isChainValid() {
        for (int i = 1; i < chain.size(); i++) {
            Block current = chain.get(i);
            Block previous = chain.get(i - 1);

            // Check 1: does the block's stored hash still match a fresh calculation?
            if (!current.getHash().equals(current.calculateHash())) {
                return false;
            }

            // Check 2: does this block's previousHash actually point to the previous block?
            if (!current.getPreviousHash().equals(previous.getHash())) {
                return false;
            }
        }
        return true;
    }

    public List<Block> getChain() {
        return Collections.unmodifiableList(chain);
    }

    public Block getLastBlock() {
        return chain.getLast();
    }

    public int getLength() {
        return chain.size();
    }
}
