package ca.midnight.classloaders;

import ca.midnight.classloaders.cryptors.Cryptors;

public class Main {
	
	public static void main(String[] args) throws Exception {
		String s = "1F8B08000000000000007D53DB6ED340103D9BB8716C0C2D0E292D509A724DDA52730729888754201E5CA894D207F2B47196642B5F2A7B835AFE0A1E20E2810FE0A310B30E345409D8D2CEEEECCC9CE333E31F3FBF7D07F0104F4C14185602EE45B217CBFE407941C8B32C4C784FA499B7C3656CC2605838E01FB817F2B8EFBDE91E884031949EC958AAE70CC57A639FC1D84E7AC246119683399418E67D198BD7C3A82BD23DDE0D0583EB27010FF7792AF5F9B7D350039931ACFAFF25D1A4C0882CC362BDE34FC8B4552AE37E5313B05F1C05E250C924CE4CB80C9549D4C98D8D0B58345165A8FD1BAEF556C9307370114B0C5626886E283F0A0DFD17F2B6CE69BE6B745ABAE8250797718548748F95C8F69257E228A7DA6A4C93B570153513ABA7546D1F674A440ED6708D144D862470759C2A136F97F214650B1E35CBB8C1C0320D7ACBC16DD4A913EF9334E294F1B43E8D3643AC69978D756C9AD8F8A3DA694C0777B0C5601E6A57483DA8CEC0C96780A77DEA65654687489A13257B0C854E8B3E73A055726706B793611A8897528F88A527604B47913C9406FD1468477346AB49278F2C233BB7FE15E54FF9B54D6B297716718656671C40F62C590BE7304F510BB437E8E67C5E26A0585D7ED965C608157F6384E59DCF5871AF6F8E70F30B1A93D22EA56900935E07652C51490D531B17204677C9D224E21EEE1380867D40BF9CE6FD28AFF2F81777FB069E87030000";
		ByteArrayClassLoader cl = new ByteArrayClassLoader();
		MClass klass = new MClass(s);
		cl.addClass(klass);
		System.out.println(klass.format("unedited"));
		System.out.println(klass.encrypt(Cryptors.BASIC).format("encrypted"));
		System.out.println(klass.decrypt(Cryptors.BASIC).format("decrypted"));
	}
}

