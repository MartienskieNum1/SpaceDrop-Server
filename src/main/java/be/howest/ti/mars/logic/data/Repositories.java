package be.howest.ti.mars.logic.data;

public class Repositories {

    public static final MarsRepository H2REPO = H2Repository.getInstance();
    public static final MarsRepository MOCK_REPO = MockRepository.getInstance();

    private Repositories() {
    }
}
