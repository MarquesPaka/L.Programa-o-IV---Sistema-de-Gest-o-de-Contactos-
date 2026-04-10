import java.io.*;
import java.util.*;

// =========================
// Classe Contacto
// =========================
class Contacto {
    private String nome;
    private String telefone;
    private String email;

    public Contacto(String nome, String telefone, String email) {
        this.nome = nome;
        this.telefone = telefone;
        this.email = email;
    }

    public String getNome() { return nome; }
    public String getTelefone() { return telefone; }
    public String getEmail() { return email; }

    public void setNome(String nome) { this.nome = nome; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public void setEmail(String email) { this.email = email; }

    @Override
    public String toString() {
        return "\n Nome: " + nome + " |\n Telefone: " + telefone + " | \n Email: " + email;
    }

    public String toFile() {
        return nome + ";" + telefone + ";" + email;
    }

    public static Contacto fromFile(String linha) {
        String[] dados = linha.split(";");
        return new Contacto(dados[0], dados[1], dados[2]);
    }
}

// =========================
// Sistema de Contactos
// =========================
class SistemaContactos {
    private List<Contacto> contactos = new ArrayList<>();

    // =========================
    // Validação Telefone
    // =========================
    private boolean telefoneValido(String telefone) {
        if (telefone == null || telefone.length() != 9) return false;

        for (int i = 0; i < telefone.length(); i++) {
            if (!Character.isDigit(telefone.charAt(i))) {
                System.out.println("Erro: apenas números são permitidos (posição " + (i + 1) + ")");
                return false;
            }
        }
        return true;
    }

    // =========================
    // Validação Email
    // =========================
    private boolean emailValido(String email) {
        return email != null && email.endsWith("@gmail.com") && !email.trim().isEmpty();
    }

    // =========================
    // Adicionar
    // =========================
    public void adicionar(Contacto c) {

        if (c.getNome().trim().isEmpty() ||
                c.getTelefone().trim().isEmpty() ||
                c.getEmail().trim().isEmpty()) {

            System.out.println("Erro: Todos os campos são obrigatórios!");
            return;
        }

        String tel = c.getTelefone().replace("+244", "").trim();

        if (!telefoneValido(tel)) {
            System.out.println("Telefone inválido! Deve conter 9 dígitos.");
            return;
        }

        if (!emailValido(c.getEmail())) {
            System.out.println("Email inválido! Deve terminar com @gmail.com");
            return;
        }

        String telefoneCompleto = "+244" + tel;

        contactos.add(new Contacto(c.getNome(), telefoneCompleto, c.getEmail()));
        System.out.println("Contacto adicionado com sucesso!");
    }

    // =========================
    // Listar
    // =========================
    public void listar() {
        if (contactos.isEmpty()) {
            System.out.println("Nenhum contacto encontrado.");
            return;
        }

        for (int i = 0; i < contactos.size(); i++) {
            System.out.println((i + 1) + " - " + contactos.get(i));
        }
    }

    // =========================
    // Pesquisar
    // =========================
    public void pesquisar(String termo) {
        boolean encontrado = false;

        for (Contacto c : contactos) {
            if (c.getNome().toLowerCase().contains(termo.toLowerCase()) ||
                    c.getTelefone().contains(termo) ||
                    c.getEmail().toLowerCase().contains(termo.toLowerCase())) {

                System.out.println(c);
                encontrado = true;
            }
        }

        if (!encontrado) {
            System.out.println("Nenhum contacto encontrado.");
        }
    }

    // =========================
    // Editar
    // =========================
    public void editar(int index, Scanner sc) {
        index = index - 1;

        if (index >= 0 && index < contactos.size()) {
            Contacto c = contactos.get(index);

            System.out.println("O que deseja editar?");
            System.out.println("1. Nome");
            System.out.println("2. Telefone");
            System.out.println("3. Email");
            System.out.print("Escolha: ");

            int opcao = sc.nextInt();
            sc.nextLine();

            switch (opcao) {
                case 1:
                    System.out.print("Novo nome: ");
                    String nome = sc.nextLine();

                    if (nome.trim().isEmpty()) {
                        System.out.println("Nome inválido!");
                        return;
                    }

                    c.setNome(nome);
                    break;

                case 2:
                    System.out.print("Novo telefone (+244): ");
                    String telefone = sc.nextLine();

                    String tel = telefone.replace("+244", "").trim();

                    if (!telefoneValido(tel)) {
                        System.out.println("Telefone inválido!");
                        return;
                    }

                    c.setTelefone("+244" + tel);
                    break;

                case 3:
                    System.out.print("Novo email: ");
                    String email = sc.nextLine();

                    if (email == null || email.trim().isEmpty()) {
                        System.out.println("Email não pode estar vazio!");
                        return;
                    }

                    if (!emailValido(email)) {
                        System.out.println("Email inválido! Deve terminar com @gmail.com");
                        return;
                    }

                    c.setEmail(email);
                    break;

                default:
                    System.out.println("Opção inválida!");
                    return;
            }

            System.out.println("Contacto atualizado com sucesso!");
        } else {
            System.out.println("Índice inválido!");
        }
    }

    // =========================
    // Remover
    // =========================
    public void remover(int index) {
        index = index - 1;

        if (index >= 0 && index < contactos.size()) {
            contactos.remove(index);
            System.out.println("Contacto removido com sucesso!");
        } else {
            System.out.println("Índice inválido!");
        }
    }

    // =========================
    // Guardar
    // =========================
    public void guardar(String ficheiro) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ficheiro))) {
            for (Contacto c : contactos) {
                bw.write(c.toFile());
                bw.newLine();
            }
            System.out.println("Dados guardados com sucesso!");
        } catch (IOException e) {
            System.out.println("Erro ao guardar ficheiro.");
        }
    }

    // =========================
    // Carregar
    // =========================
    public void carregar(String ficheiro) {
        contactos.clear();

        File f = new File(ficheiro);

        if (!f.exists()) {
            System.out.println("Ficheiro ainda não existe. Será criado automaticamente.");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(ficheiro))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                contactos.add(Contacto.fromFile(linha));
            }
            System.out.println("Dados carregados com sucesso!");
        } catch (IOException e) {
            System.out.println("Erro ao carregar ficheiro.");
        }
    }
}

// =========================
// MAIN
// =========================
public class Main {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        SistemaContactos sistema = new SistemaContactos();

        // CARREGAR AUTOMATICAMENTE
        sistema.carregar("contactos.txt");

        int opcao;

        do {
            System.out.println("\n===== SISTEMA DE CONTACTOS =====");
            System.out.println("1. Adicionar Contacto");
            System.out.println("2. Listar Contactos");
            System.out.println("3. Editar Contacto");
            System.out.println("4. Remover Contacto");
            System.out.println("5. Guardar em Ficheiro");
            System.out.println("6. Carregar Ficheiro");
            System.out.println("7. Pesquisar Contacto");
            System.out.println("0. Sair");
            System.out.print("Escolha: ");

            opcao = sc.nextInt();
            sc.nextLine();

            switch (opcao) {
                case 1:
                    System.out.print("Nome: ");
                    String nome = sc.nextLine();

                    System.out.print("Telefone (9 dígitos): ");
                    String telefone = sc.nextLine();

                    System.out.print("Email: ");
                    String email = sc.nextLine();

                    sistema.adicionar(new Contacto(nome, telefone, email));
                    sistema.guardar("contactos.txt");
                    break;

                case 2:
                    sistema.listar();
                    break;

                case 3:
                    sistema.listar();
                    System.out.print("Índice do contacto: ");
                    sistema.editar(sc.nextInt(), sc);
                    sistema.guardar("contactos.txt");
                    break;

                case 4:
                    sistema.listar();
                    System.out.print("Índice do contacto: ");
                    sistema.remover(sc.nextInt());
                    sistema.guardar("contactos.txt");
                    break;

                case 5:
                    sistema.guardar("contactos.txt");
                    break;

                case 6:
                    sistema.carregar("contactos.txt");
                    break;

                case 7:
                    System.out.print("Digite nome, telefone ou email: ");
                    String termo = sc.nextLine();
                    sistema.pesquisar(termo);
                    break;

                case 0:
                    System.out.println("Sistema encerrado.");
                    break;

                default:
                    System.out.println("Opção inválida!");
            }

        } while (opcao != 0);

        sc.close();
    }
}