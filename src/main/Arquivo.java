package main;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class Arquivo {

    private final Path caminho = Paths.get("compras.txt");

    public void salvarCompra(String nome, String categoria, String formaPagamento,
                             float valorOriginal, int descontoPercentual,
                             float valorPago) throws IOException {

        if (nome == null || nome.isBlank() || nome.contains(";")
                || nome.contains("\n") || nome.contains("\r")) {
            throw new IllegalArgumentException("Nome inválido para gravar a compra.");
        }

        String dataHora = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));

        String linha = String.format(Locale.ROOT, "%s;%s;%s;%s;%.2f;%d;%.2f%n",
                dataHora, nome, categoria, formaPagamento,
                valorOriginal, descontoPercentual, valorPago);

        // Substituição atômica evita deixar uma linha parcial ao falhar a gravação.
        // Adequado para uma única execução do programa por vez.
        Path destino = caminho.toAbsolutePath();
        String anterior = Files.exists(destino) ? Files.readString(destino) : "";
        Path temporario = Files.createTempFile(destino.getParent(), "compras-", ".tmp");
        try {
            Files.writeString(temporario, anterior + linha);
            Files.move(temporario, destino, StandardCopyOption.ATOMIC_MOVE,
                    StandardCopyOption.REPLACE_EXISTING);
        } finally {
            Files.deleteIfExists(temporario);
        }
    }

    public void listarCompras() throws IOException {
        if (Files.notExists(caminho)) {
            System.out.println("Nenhuma compra registrada.");
            return;
        }

        List<String> linhas = Files.readAllLines(caminho);

        for (String linha : linhas) {
            String[] campos = linha.split(";", -1);

            if (campos.length != 7) {
                System.out.println("Registro de compra com formato inválido.");
                continue;
            }

            System.out.println(campos[0] + " | " + campos[1]
                    + " (" + campos[2] + ") | " + campos[3]
                    + " | Original: R$ " + campos[4]
                    + " | Desconto: " + campos[5] + "%"
                    + " | Pago: R$ " + campos[6]);
        }
    }
}
