package org.example;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Locale.US garante uso de ponto (.) nos decimais, compatível com o CSV
        Scanner input = new Scanner(System.in).useLocale(Locale.US);
        File arquivo = new File("relatorio.csv");

        double[] volume = new double[7];
        String[] diasSemana = {
                "segunda-feira", "terça-feira", "quarta-feira",
                "quinta-feira", "sexta-feira", "sábado", "domingo"
        };

        int linha = 0;
        int coluna = 0;
        double[][] umidade = new double[4][4];
        int escolha = 0;
        boolean dados = false;

        do {
            System.out.println("""
                    1) Cadastrar Dados
                    2) Exibir Mapa do Campo
                    3) Relatório de Alertas de Irrigação
                    4) Sair
                    """);
            escolha = input.nextInt();

            switch (escolha) {
                case 1:
                    input.nextLine(); // Limpa o buffer do teclado após o nextInt()
                    System.out.print("Digite a data da semana (ex: 10/11/2026): ");
                    String dataSemana = input.nextLine();

                    double totalChuva = 0;

                    for (int i = 0; i < volume.length; i++) {
                        System.out.print("Digite o volume da chuva (em mm) de " + diasSemana[i] + ": ");
                        volume[i] = input.nextDouble();
                        totalChuva += volume[i];
                    }

                    double mediaChuva = totalChuva / 7.0;

                    // Verifica se o arquivo já existe para saber se precisa criar o cabeçalho
                    boolean arquivoNovo = !arquivo.exists() || arquivo.length() == 0;

                    // O parâmetro 'true' no FileWriter garante que não sobrescreva semanas anteriores
                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(arquivo, true))) {
                        if (arquivoNovo) {
                            writer.write("data,seg,ter,qua,qui,sex,sab,dom,media,total");
                            writer.newLine();
                        }

                        // Monta a linha no formato CSV esperado
                        String registro = String.format(Locale.US,
                                "%s,%.1f,%.1f,%.1f,%.1f,%.1f,%.1f,%.1f,%.1f,%.1f",
                                dataSemana,
                                volume[0], volume[1], volume[2], volume[3], volume[4], volume[5], volume[6],
                                mediaChuva, totalChuva
                        );

                        writer.write(registro);
                        writer.newLine();
                        System.out.println("Dados gravados com sucesso em relatorio.csv!\n");

                    } catch (IOException e) {
                        System.out.println("Erro ao salvar no arquivo CSV: " + e.getMessage());
                    }
                    break;

                case 2:
                    if (!dados) {
                        for (int i = 0; i <= 15; i++) {
                            System.out.println("Qual a umidade do talhão " + (linha + 1) + "X" + (coluna + 1));
                            System.out.println("""
                                       1 2 3 4
                                    1  X X X X
                                    2  X X X X
                                    3  X X X X
                                    4  X X X X
                                    """);
                            umidade[linha][coluna] = input.nextDouble();
                            linha++;

                            if (linha == 4) {
                                linha = 0;
                                coluna++;
                            }
                            if (i == 15) {
                                dados = true;
                            }
                        }
                    } else {
                        System.out.println("   1    2    3    4");
                        System.out.println("1 " + umidade[0][0] + " " + umidade[0][1] + " " + umidade[0][2] + " " + umidade[0][3]);
                        System.out.println("2 " + umidade[1][0] + " " + umidade[1][1] + " " + umidade[1][2] + " " + umidade[1][3]);
                        System.out.println("3 " + umidade[2][0] + " " + umidade[2][1] + " " + umidade[2][2] + " " + umidade[2][3]);
                        System.out.println("4 " + umidade[3][0] + " " + umidade[3][1] + " " + umidade[3][2] + " " + umidade[3][3]);
                    }
                    break;

                case 3:
                    if (!dados) {
                        System.out.println("Cadastre os dados primeiros!");
                    } else {
                        boolean alerta = false;

                        for (int l = 0; l < 4; l++) {
                            for (int c = 0; c < 4; c++) {
                                double umidadeTalhao = umidade[l][c];

                                if (umidadeTalhao < 30) {
                                    System.out.println("O talhão " + (l + 1) + "X" + (c + 1) + " precisa de irrigação.");
                                    alerta = true;
                                }
                            }
                        }

                        if (!alerta) {
                            System.out.println("Todos os setores estão bem irrigados.");
                        }
                    }
                    break;

                case 4:
                    System.out.println("Sistema encerrando...");
                    break;

                default:
                    System.out.println("Opção inválida! Escolha um número de 1-4.");
            }
        } while (escolha != 4);

        input.close();
    }
}