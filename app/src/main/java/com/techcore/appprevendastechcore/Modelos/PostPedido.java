package com.techcore.appprevendastechcore.Modelos;

import java.io.Serializable;

/**
 * Created by Winston on 12/09/2017.
 */

public class PostPedido implements Serializable {
    private String Status;

    public PostPedido(String status) {
        Status = status;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public class PostProdutos implements Serializable {
        private String Status;

        public PostProdutos(String status) {
            Status = status;
        }

        public String getStatus() {
            return Status;
        }

        public void setStatus(String status) {
            Status = status;
        }
    }
}



