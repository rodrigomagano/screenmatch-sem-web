package br.com.alura.screenmatch.principal;

import br.com.alura.screenmatch.model.DadosSerie;
import br.com.alura.screenmatch.model.DadosTemporada;
import br.com.alura.screenmatch.service.ConsumoApi;
import br.com.alura.screenmatch.service.ConverteDados;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Principal {

    private  Scanner leitura = new Scanner(System.in);

    private ConsumoApi consumoapi = new ConsumoApi();

   private ConverteDados conversor = new ConverteDados();

    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String APIKEY = "&apikey=7c9249f7";

    public void exibeMenu() {
        System.out.println("Digite o nome da série:");
        var nomeSerie = leitura.nextLine();

        var json = consumoapi.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + APIKEY);

        DadosSerie dados = conversor.obterdados(json, DadosSerie.class);

        if (dados.titulo() == null || dados.titulo().equalsIgnoreCase("N/A")) {
            System.out.println("Série não encontrada. Verifique o nome digitado.");
            return;
        }

        System.out.println(dados); // Exibe os dados recebidos

        Integer temporadas = dados.totalTemporadas();

        if (temporadas == null) {
            System.out.println("Número de temporadas não disponível.");
            return;
        }

        List<DadosTemporada> listaTemporadas = new ArrayList<>();

        for (int i = 1; i <= temporadas; i++) {
            json = consumoapi.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + "&season=" + i + APIKEY);
            DadosTemporada dadosTemporada = conversor.obterdados(json, DadosTemporada.class);
            listaTemporadas.add(dadosTemporada);
        }

        listaTemporadas.forEach(System.out::println);
        listaTemporadas.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())));
    }
}
