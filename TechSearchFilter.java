package GUIComponents;

/**
 * An enumeration for any ticket filter options you may want.
 *
 * HINT:  Add more filters for filtering on a tech's own ID
 *        or anotehr tech's ID.
 * 
 * @author ryank
 *
 *
 */
public enum TechSearchFilter {

	NONE, CREATE, SEARCH; //List of all possible filters - Only Filters that a user might neeed have been added thus far.
						 
	
	private static TechSearchFilter[] techFilters =  {NONE, CREATE, SEARCH};  //just the ones users would need.
	/***
	 * Returns those filters appropriate for the given role.
	 * @param role The role of the person (The role defines which filters are possible)
	 * @return  An array of search filters allowed for users.
	 */
	public static TechSearchFilter[] getFiltersForRole()
	{
		
		return techFilters;
		
	}
}
