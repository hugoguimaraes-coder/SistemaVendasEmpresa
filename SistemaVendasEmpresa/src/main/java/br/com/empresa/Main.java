package br.com.empresa;

import br.com.empresa.model.Vendedor;
import br.com.empresa.repository.VendedorRepository;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner leitor = new Scanner(System.in);
        VendedorRepository repo = new VendedorRepository();
        int opcao = -1;

        while (opcao != 0) {
            System.out.println("\n--- GESTÃO DE VENDEDORES (ADMIN) ---");
            System.out.println("1. Cadastrar Novo Vendedor");
            System.out.println("2. Alterar Nome/Comissão (Update)");
            System.out.println("3. Deletar Vendedor");
            System.out.println("4. Listar Todos os Vendedores");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");

            opcao = leitor.nextInt();
            leitor.nextLine(); // Limpar o buffer do teclado

            switch (opcao) {
                case 1:
                    System.out.print("Nome do Vendedor: ");
                    String nome = leitor.nextLine();
                    System.out.print("Percentual de Comissão: ");
                    double comissao = leitor.nextDouble();

                    Vendedor novo = new Vendedor(nome, comissao);
                    repo.salvar(novo);
                    break;

                case 2:
                    System.out.print("ID do Vendedor que deseja alterar: ");
                    int idAlt = leitor.nextInt();
                    leitor.nextLine(); // Limpa buffer
                    System.out.print("Novo Nome: ");
                    String novoNome = leitor.nextLine();
                    System.out.print("Nova Comissão: ");
                    double novaComis = leitor.nextDouble();

                    Vendedor vAlt = new Vendedor();
                    vAlt.setId(idAlt);
                    vAlt.setNome(novoNome);
                    vAlt.setPercentualComissao(novaComis);
                    repo.atualizar(vAlt);
                    break;

                case 3:
                    System.out.print("Digite o ID do Vendedor para DELETAR: ");
                    int idDel = leitor.nextInt();
                    repo.deletar(idDel);
                    break;
                case 4:
                    System.out.println("\n--- LISTA DE VENDEDORES ---");
                    List<Vendedor> vendedores = repo.listarTodos();

                    if (vendedores.isEmpty()) {
                        System.out.println("Nenhum vendedor cadastrado.");
                    } else {
                        // Cabeçalho formatado
                        System.out.printf("%-5s | %-20s | %-10s%n", "ID", "NOME", "COMISSÃO %");
                        System.out.println("------------------------------------------");
                        for (Vendedor v : vendedores) {
                            System.out.printf("%-5d | %-20s | %-10.2f%%%n",
                                    v.getId(), v.getNome(), v.getPercentualComissao());
                        }
                    }
                    break;

                case 0:
                    System.out.println("Encerrando sistema...");
                    break;

                default:
                    System.out.println("Opção inválida!");
            }
        }
        leitor.close();
    }
}