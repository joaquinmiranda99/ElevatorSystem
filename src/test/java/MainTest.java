import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MainTest {

    @Test
    public void testElevatorCarga(){
        Main.Elevator elevator = new Main.Elevator(Main.TypeElevator.MONTACARGA);

        Main.Carga carga1 = new Main.Carga(500);
        Main.Carga carga2 = new Main.Carga(3500);

        List<Main.UnknowCarga> cargas = new ArrayList<>();
        cargas.add(carga1);
        assertEquals(true, elevator.goToFloor(20, cargas));

        cargas.add(carga2);
        assertEquals(false, elevator.goToFloor(20, cargas));
    }

    @Test
    public void testAuthPerson() {
        Main.Elevator elevator = new Main.Elevator(Main.TypeElevator.PUBLICO);

        Main.Person person1 = new Main.Person("Joaco", 100);
        Main.Person person2 = new Main.Person("Julian", 100);

        List<Main.UnknowCarga> personas = new ArrayList<>();

        personas.add(person1);
        personas.add(person2);

        Main.AccessCard person1AccessCard = new Main.AccessCard("Joaco", "123");
        Main.AccessCard person2AccessCard = new Main.AccessCard("Lautaro", "156");

        // Se agrega la tarjeta de acceso de la persona al sistema
        elevator.addAccessCard(person1AccessCard, "ADMIN123");

        // Usando la tarjeta de acceso de persona 1 lo va a dejar
        assertEquals(true, elevator.goToFloor(-1, person1AccessCard, personas));

        // Usando la tarjeta de acceso de la persona 2, como no esta en el sistema, no lo va a dejar
        assertEquals(false, elevator.goToFloor(-1, person2AccessCard, personas));
    }

    @Test
    public void testCarga() {
        Main.Elevator elevator = new Main.Elevator(Main.TypeElevator.PUBLICO);

        Main.Person person1 = new Main.Person("Joaco", 100);
        Main.Person person2 = new Main.Person("Julian", 100);

        List<Main.UnknowCarga> personas = new ArrayList<>();

        personas.add(person1);
        personas.add(person2);

        Main.AccessCard person1AccessCard = new Main.AccessCard("Joaco", "123");

        // Se agrega la tarjeta de acceso de la persona al sistema
        elevator.addAccessCard(person1AccessCard, "ADMIN123");

        // Como no supero el peso permitido, le permite ir al piso
        assertEquals(true, elevator.goToFloor(-1, person1AccessCard, personas));

        // Agregamos 10 personas a la carga
        for (int i = 0; i < 10; i++) {
            personas.add(person1);
        }

        // Como supero el peso permitido, no le permite ir al piso
        assertEquals(false, elevator.goToFloor(-1, person1AccessCard, personas));
    }
}
