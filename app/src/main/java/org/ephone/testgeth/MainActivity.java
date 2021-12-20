package org.ephone.testgeth;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.ephone.testgeth.EnsResolver;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

import java.io.IOException;

import geth.*;

public class MainActivity extends AppCompatActivity {
    TextView textView;
    Node node;
    Context ctx;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.textView);
        NodeConfig nodeConfig = Geth.newNodeConfig();
        Geth.setVerbosity(4);
        ctx = Geth.newContext();
        try {
            node = Geth.newNode(getFilesDir()+"/.ethNode", nodeConfig);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            node.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void outputData(View w) throws IOException {
        try {
            System.out.println("Peersize: "+node.getPeersInfo().size());
            textView.setText("Peersize: " + node.getPeersInfo().size() + "\n");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        System.out.println("Name: "+node.getNodeInfo().getName());
        textView.append("Name: " + node.getNodeInfo().getName() + "\n");
        try {
            System.out.println("Current Block: "+node.getEthereumClient().getBlockByNumber(ctx, -1).getNumber());
            textView.append("Current Block: "+node.getEthereumClient().getBlockByNumber(ctx, -1).getNumber()+"\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            System.out.println("Balance (Eth2 Deposit): "+node.getEthereumClient().getBalanceAt(ctx, new Address("0x00000000219ab540356cBB839Cbe05303d7705Fa"), -1));
            textView.append("Balance (Eth2 Deposit): "+node.getEthereumClient().getBalanceAt(ctx, new Address("0x00000000219ab540356cBB839Cbe05303d7705Fa"), -1)+"\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Runnable run = new Runnable() {
            @Override
            public void run() {
                Web3j web3j = Web3j.build(new HttpService("http://127.0.0.1:8545"));
                try {
                    EnsResolver ensResolver = new EnsResolver(web3j);
                    System.out.println("Get Blocknumber from node: " + web3j.ethBlockNumber().send().getBlockNumber().toString());
                    System.out.println("Get ENS Address: " + ensResolver.resolve("mhaas.eth"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new Thread(run).start();
    }


}