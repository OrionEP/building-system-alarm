package bms.util;

/**
 * A type that can be encoded to a machine-readable string representation,
 * useful for saving objects to files.
 */
public interface Encodable {
    /**
     * the String representation of the current state of this object.
     *
     * @return String encoded String representation.
     */
    String encode();
}
