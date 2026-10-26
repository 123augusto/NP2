package pagamento;

public abstract interface Cartao {//interface abstrata
    public abstract boolean validar();
    public abstract boolean autorizar(float valor);
}
