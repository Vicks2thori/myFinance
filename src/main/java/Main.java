import Category.CategoryController;
import Category.CategoryDTO;
import Category.CategoryRepository;
import Category.CategoryService;

public class Main {

    public static void main(String[] args) {

        CategoryRepository repository = new CategoryRepository();
        CategoryService service = new CategoryService(repository);
        CategoryController controller = new CategoryController(service);

        try {
            CategoryDTO novaCategoria = new CategoryDTO(
                    null,
                    "Alimentação",
                    "Gastos com supermercado e refeições",
                    true,
                    null,
                    null,
                    null
            );

            CategoryDTO cadastrada =
                    controller.cadastrar(novaCategoria);

            System.out.println("Categoria cadastrada:");
            System.out.println(cadastrada);

            System.out.println("\nCategorias existentes:");
            controller.listarTodos()
                    .forEach(System.out::println);

            System.out.println("\nCategoria pelo ID:");
            System.out.println(
                    controller.buscarPorId(cadastrada.id())
            );

        } catch (Exception e) {
            System.err.println("Erro: " + e.getMessage());
        }
    }
}