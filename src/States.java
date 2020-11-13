public enum States {
    THINKING("Pensando"),
    HUNGRY("Tentando comer"),
    EATING("Comendo");

    public String label;

    States(String label) {
        this.label = label;
    }
}
