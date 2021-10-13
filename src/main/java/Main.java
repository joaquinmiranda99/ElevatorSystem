import java.util.ArrayList;
import java.util.List;

public class Main {

    static class Elevator {
        private int currentFloor = 0;
        private AccessSystem accessSystem;

        public Elevator (TypeElevator typeElevator) {
            this.accessSystem = new AccessSystem("ADMIN123", typeElevator);
        }

        public boolean goToFloor(int floor, List<UnknowCarga> unknowCargas) {
            try {
                if (accessSystem.hasAuthorityToFloor(floor, unknowCargas)) {
                    System.out.println("Se accedio al piso: " + floor + ", correctamente");
                    this.currentFloor = floor;
                    return true;
                }
                return false;
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return false;
            }
        }

        public boolean goToFloor(int floor, AccessCard accessCard, List<UnknowCarga> unknowCargas) {
            try {
                if (accessSystem.hasAuthorityToFloor(floor, accessCard, unknowCargas)) {
                    System.out.println(accessCard.name + " accedio al piso: " + floor + ", correctamente");
                    this.currentFloor = floor;
                    return true;
                } else {
                    System.out.println(accessCard.name + " no tiene acceso para ir al piso: " + floor);
                    return false;
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return false;
            }
        }

        public void addAccessCard(AccessCard accessCard, String password) {
            try {
                this.accessSystem.createCard(accessCard, password);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    static class AccessSystem {
        List<Integer> hashCodes = new ArrayList<>();
        private String password;
        private TypeElevator type;

        public AccessSystem(String password, TypeElevator typeElevator) {
            this.password = password;
            this.type = typeElevator;
        }

        public void createCard (AccessCard accessCard, String password) throws Exception {
            if (password.equals(this.password)) {
                hashCodes.add(accessCard.hashCode());
            } else {
                throw new Exception("Contrasena incorrecta");
            }
        }

        public boolean hasAuthorityToFloor(int floor, List<UnknowCarga> unknowCargas) throws Exception {
            int peso = unknowCargas.stream().map(u -> u.getWeight()).reduce(0, (a, b) -> a + b);
            switch (type) {
                case PUBLICO:
                    for (UnknowCarga carga : unknowCargas) {
                        if (carga instanceof Carga) {
                            throw new Exception("No puedes ingresar cargas al ascensor publico");
                        }
                    }
                    if (peso < 1000) {
                        if (floor == -1 || floor == 50) {
                            throw new Exception("Necesita tarjeta de acceso para el piso: " + floor);
                        } else {
                            return true;
                        }
                    } else {
                        throw new Exception("La carga de " + peso + " excedio el peso permitido: 1000");
                    }
                default:
                    for (UnknowCarga carga : unknowCargas) {
                        if (carga instanceof Person) {
                            throw new Exception("No puedes ingresar personas al montacargas");
                        }
                    }
                    if (peso < 3000) {
                        return true;
                    } else {
                        throw new Exception("La carga de " + peso + " excedio el peso permitido: 3000");
                    }
            }
        }

        public boolean hasAuthorityToFloor(int floor, AccessCard accessCard, List<UnknowCarga> unknowCargas) throws Exception {
            int peso = unknowCargas.stream().map(u -> u.getWeight()).reduce(0, (a, b) -> a + b);
            switch (type) {
                case PUBLICO:
                    for (UnknowCarga carga : unknowCargas) {
                        if (carga instanceof Carga) {
                            throw new Exception("No puedes ingresar cargas al ascensor publico");
                        }
                    }
                    if (peso < 1000) {
                        return floor == -1 || floor == 50 ? hashCodes.contains(accessCard.hashCode()) : true;
                    } else {
                        throw new Exception("La carga de " + peso + " excedio el peso permitido: 1000");
                    }
                default:
                    for (UnknowCarga carga : unknowCargas) {
                        if (carga instanceof Person) {
                            throw new Exception("No puedes ingresar personas al montacargas");
                        }
                    }
                    System.out.println("Se accedio al piso: " + floor + " correctamente");
                    return peso < 3000;
            }
        }
    }

    static class AccessCard {
        private String name;
        private String password;

        public AccessCard(String name, String password) {
            this.name = name;
            this.password = password;
        }
    }

    interface UnknowCarga {
        int getWeight();
    }

    static class Person implements UnknowCarga {
        private int weight;
        private String name;

        public Person(String name, int weight) {
            this.name = name;
            this.weight = weight;
        }

        @Override
        public int getWeight() {
            return weight;
        }
    }

    static class Carga implements UnknowCarga {
        private int weight;

        public Carga(int weight) {
            this.weight = weight;
        }

        @Override
        public int getWeight() {
            return weight;
        }
    }

    enum TypeElevator {
        PUBLICO, MONTACARGA
    }
}
