public class Test {

    public static void main(String[] args) {
        MerkleHash hash = MerkleHash.create("hello");
        System.out.println(hash);
        System.out.println(MerkleHash.create("hello").equals(hash));
    }
}
