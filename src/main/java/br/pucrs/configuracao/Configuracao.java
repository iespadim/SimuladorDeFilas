package br.pucrs.configuracao;

import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;

public class Configuracao {
    public static Map<String, Object> carregarConfiguracoes(String caminhoArquivo) {
        Yaml yaml = new Yaml();
        try {
            //InputStream inputStream = Configuracao.class.getClassLoader().getResourceAsStream(caminhoArquivo);
            InputStream inputStream = new FileInputStream("src/config/configuracao.yaml");
            if (inputStream == null) {
                System.out.println("Arquivo não encontrado: " + caminhoArquivo);
                return null;
            }
            return yaml.load(inputStream);  // Carregar o arquivo YAML
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}