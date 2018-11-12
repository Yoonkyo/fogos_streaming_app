package flexid;

public class FlexID implements FlexIDInterface {
    private byte[] identity;         // The hash value of the public key
    private byte[] priv;             // The private key corresponding to the above public key
    private FlexIDType type;          // The type of Flex ID
    private AttrValuePairs avps;      // The attribute-value pairs of Flex ID
    private Locator loc;              // The locator

    public FlexID() {

    }

    public FlexID(byte[] identity, FlexIDType type, AttrValuePairs avps, Locator loc) {
        this.identity = identity;
        this.type = type;
        this.avps = avps;
        this.loc = loc;
    }

    public byte[] getIdentity() {
        return identity;
    }

    public void setIdentity(byte[] identity) {
        this.identity = identity;
    }

    public FlexIDType getType() {
        return type;
    }

    public void setType(FlexIDType type) {
        this.type = type;
    }

    public AttrValuePairs getAvps() {
        return avps;
    }

    public void setAvps(AttrValuePairs avps) {
        this.avps = avps;
    }

    public Locator getLoc() {
        return loc;
    }

    public void setLoc(Locator loc) {
        this.loc = loc;
    }

    @Override
    public String getValueByAttr(String attr) {
        return null;
    }

    @Override
    public Locator getLocator() {
        return null;
    }

    @Override
    public void updateLocator(Locator loc) {

    }
}
