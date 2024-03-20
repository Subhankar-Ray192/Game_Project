class A
{
	enum SymbolAB
	{
		X(1), O(2), EMPTY(0);
		
		private final int val;
		
		SymbolAB(int val)
		{
			this.val = val;
		}
		
		public int getValue()
		{
			return val;
		}
		
		@Override
		public String toString()
		{
			switch(this)
			{
				case X:
					return "X";
				case O:
					return "O";
				case EMPTY:
					return "_";
				default:
					return "";
			}
		}
	}
	
	public static void main(String args[])
	{
		SymbolAB sym1 = SymbolAB.X;
		System.out.println(sym1);
	}
}