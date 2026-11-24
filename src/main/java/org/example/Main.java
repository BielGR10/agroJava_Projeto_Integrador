package org.example;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

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
                    4) Consultar Histórico
                    5) Identificar Semana Mais Chuvosa
                    6) Dashboard Climático
                    7) Sair
                    """);
            escolha = input.nextInt();

            switch (escolha) {
                case 1:
                    input.nextLine();
                    System.out.print("Digite a data da semana (ex: 10/11/2026): ");
                    String dataSemana = input.nextLine();

                    double totalChuva = 0;

                    for (int i = 0; i < volume.length; i++) {
                        System.out.print("Digite o volume da chuva (em mm) de " + diasSemana[i] + ": ");
                        volume[i] = input.nextDouble();
                        totalChuva += volume[i];
                    }

                    double mediaChuva = totalChuva / 7.0;

                    boolean arquivoNovo = !arquivo.exists() || arquivo.length() == 0;

                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(arquivo, true))) {
                        if (arquivoNovo) {
                            writer.write("data,seg,ter,qua,qui,sex,sab,dom,media,total");
                            writer.newLine();
                        }

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
                    if (!arquivo.exists() || arquivo.length() == 0) {
                        System.out.println("Nenhum histórico encontrado. Cadastre os dados primeiro!\n");
                        break;
                    }

                    try (BufferedReader reader = new BufferedReader(new FileReader(arquivo))) {
                        String linhaCsv = reader.readLine();
                        boolean temRegistros = false;

                        while ((linhaCsv = reader.readLine()) != null) {
                            if (linhaCsv.trim().isEmpty()) {
                                continue;
                            }

                            String[] colunas = linhaCsv.split(",");

                            System.out.println("Semana: " + colunas[0]);
                            System.out.println("Média: " + colunas[8] + " mm");
                            System.out.println("Total: " + colunas[9] + " mm");
                            System.out.println("-------------------------");
                            temRegistros = true;
                        }

                        if (!temRegistros) {
                            System.out.println("Nenhum registro salvo no histórico.\n");
                        }

                    } catch (IOException e) {
                        System.out.println("Erro ao ler o arquivo CSV: " + e.getMessage());
                    }
                    break;

                case 5:
                    if (!arquivo.exists() || arquivo.length() == 0) {
                        System.out.println("Nenhum histórico encontrado. Cadastre os dados primeiro!\n");
                        break;
                    }

                    try (BufferedReader reader = new BufferedReader(new FileReader(arquivo))) {
                        String linhaCsv = reader.readLine();
                        double maiorTotal = -1.0;
                        String semanaMaisChuvosa = "";
                        String totalFormatado = "";

                        while ((linhaCsv = reader.readLine()) != null) {
                            if (linhaCsv.trim().isEmpty()) {
                                continue;
                            }

                            String[] colunas = linhaCsv.split(",");
                            double totalAtual = Double.parseDouble(colunas[9]);

                            if (totalAtual > maiorTotal) {
                                maiorTotal = totalAtual;
                                semanaMaisChuvosa = colunas[0];
                                totalFormatado = colunas[9];
                            }
                        }

                        if (maiorTotal >= 0) {
                            System.out.println("Semana mais chuvosa:");
                            System.out.println(semanaMaisChuvosa);
                            System.out.println("Total:");
                            System.out.println(totalFormatado + " mm\n");
                        } else {
                            System.out.println("Nenhum registro salvo no histórico.\n");
                        }

                    } catch (IOException e) {
                        System.out.println("Erro ao ler o arquivo CSV: " + e.getMessage());
                    }
                    break;

                case 6:
                    if (!arquivo.exists() || arquivo.length() == 0) {
                        System.out.println("Nenhum histórico encontrado. Cadastre os dados primeiro!\n");
                        break;
                    }

                    try (BufferedReader reader = new BufferedReader(new FileReader(arquivo))) {
                        String linhaCsv = reader.readLine();
                        int semanasCadastradas = 0;
                        double volumeAcumulado = 0.0;
                        double maiorChuva = -1.0;
                        double menorChuva = Double.MAX_VALUE;

                        while ((linhaCsv = reader.readLine()) != null) {
                            if (linhaCsv.trim().isEmpty()) {
                                continue;
                            }

                            String[] colunas = linhaCsv.split(",");
                            double totalAtual = Double.parseDouble(colunas[9]);

                            semanasCadastradas++;
                            volumeAcumulado += totalAtual;

                            if (totalAtual > maiorChuva) {
                                maiorChuva = totalAtual;
                            }
                            if (totalAtual < menorChuva) {
                                menorChuva = totalAtual;
                            }
                        }

                        if (semanasCadastradas > 0) {
                            double mediaGeral = volumeAcumulado / semanasCadastradas;

                            System.out.println("===== DASHBOARD =====");
                            System.out.println("Semanas cadastradas: " + semanasCadastradas);
                            System.out.printf(Locale.US, "Volume acumulado: %.1f mm%n", volumeAcumulado);
                            System.out.println("Maior chuva:");
                            System.out.printf(Locale.US, "%.1f mm%n", maiorChuva);
                            System.out.println("Menor chuva:");
                            System.out.printf(Locale.US, "%.1f mm%n", menorChuva);
                            System.out.println("Média geral:");
                            System.out.printf(Locale.US, "%.1f mm%n%n", mediaGeral);
                        } else {
                            System.out.println("Nenhum registro salvo no histórico.\n");
                        }

                    } catch (IOException e) {
                        System.out.println("Erro ao ler o arquivo CSV: " + e.getMessage());
                    }
                    break;

                case 7:
                    System.out.println("Sistema encerrando...");
                    break;

                default:
                    System.out.println("Opção inválida! Escolha um número de 1-7.");
            }
        } while (escolha != 7);

        input.close();
    }
}