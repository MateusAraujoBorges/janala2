package tests.casestudies.math3.exception.util;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import tests.casestudies.math3.exception.util.Localizable;

public class LocalizedFormats implements Localizable {

    // CHECKSTYLE: stop MultipleVariableDeclarations
    // CHECKSTYLE: stop JavadocVariable


	public static LocalizedFormats ARGUMENT_OUTSIDE_DOMAIN() {
		   return new LocalizedFormats("Argument {0} outside domain [{1} ; {2}]");
		   }
	public static LocalizedFormats ARRAY_SIZE_EXCEEDS_MAX_VARIABLES() {
		   return new LocalizedFormats("array size cannot be greater than {0}");
		   }
	public static LocalizedFormats ARRAY_SIZES_SHOULD_HAVE_DIFFERENCE_1() {
		   return new LocalizedFormats("array sizes should have difference 1 ({0} != {1} + 1)");
		   }
	public static LocalizedFormats ARRAY_SUMS_TO_ZERO() {
		   return new LocalizedFormats("array sums to zero");
		   }
	public static LocalizedFormats ASSYMETRIC_EIGEN_NOT_SUPPORTED() {
		   return new LocalizedFormats("eigen decomposition of assymetric matrices not supported yet");
		   }
	public static LocalizedFormats AT_LEAST_ONE_COLUMN() {
		   return new LocalizedFormats("matrix must have at least one column");
		   }
	public static LocalizedFormats AT_LEAST_ONE_ROW() {
		   return new LocalizedFormats("matrix must have at least one row");
		   }
	public static LocalizedFormats BANDWIDTH() {
		   return new LocalizedFormats("bandwidth ({0})");
		   }
	public static LocalizedFormats BESSEL_FUNCTION_BAD_ARGUMENT() {
		   return new LocalizedFormats("Bessel function of order {0} cannot be computed for x = {1}");
		   }
	public static LocalizedFormats BESSEL_FUNCTION_FAILED_CONVERGENCE() {
		   return new LocalizedFormats("Bessel function of order {0} failed to converge for x = {1}");
		   }
	public static LocalizedFormats BINOMIAL_INVALID_PARAMETERS_ORDER() {
		   return new LocalizedFormats("must have n >= k for binomial coefficient (n, k), got k = {0}, n = {1}");
		   }
	public static LocalizedFormats BINOMIAL_NEGATIVE_PARAMETER() {
		   return new LocalizedFormats("must have n >= 0 for binomial coefficient (n, k), got n = {0}");
		   }
	public static LocalizedFormats CANNOT_CLEAR_STATISTIC_CONSTRUCTED_FROM_EXTERNAL_MOMENTS() {
		   return new LocalizedFormats("statistics constructed from external moments cannot be cleared");
		   }
	public static LocalizedFormats CANNOT_COMPUTE_0TH_ROOT_OF_UNITY() {
		   return new LocalizedFormats("cannot compute 0-th root of unity, indefinite result");
		   }
	public static LocalizedFormats CANNOT_COMPUTE_BETA_DENSITY_AT_0_FOR_SOME_ALPHA() {
		   return new LocalizedFormats("cannot compute beta density at 0 when alpha = {0,number}");
		   }
	public static LocalizedFormats CANNOT_COMPUTE_BETA_DENSITY_AT_1_FOR_SOME_BETA() {
		   return new LocalizedFormats("cannot compute beta density at 1 when beta = %.3g");
		   }
	public static LocalizedFormats CANNOT_COMPUTE_NTH_ROOT_FOR_NEGATIVE_N() {
		   return new LocalizedFormats("cannot compute nth root for null or negative n: {0}");
		   }
	public static LocalizedFormats CANNOT_DISCARD_NEGATIVE_NUMBER_OF_ELEMENTS() {
		   return new LocalizedFormats("cannot discard a negative number of elements ({0})");
		   }
	public static LocalizedFormats CANNOT_FORMAT_INSTANCE_AS_3D_VECTOR() {
		   return new LocalizedFormats("cannot format a {0} instance as a 3D vector");
		   }
	public static LocalizedFormats CANNOT_FORMAT_INSTANCE_AS_COMPLEX() {
		   return new LocalizedFormats("cannot format a {0} instance as a complex number");
		   }
	public static LocalizedFormats CANNOT_FORMAT_INSTANCE_AS_REAL_VECTOR() {
		   return new LocalizedFormats("cannot format a {0} instance as a real vector");
		   }
	public static LocalizedFormats CANNOT_FORMAT_OBJECT_TO_FRACTION() {
		   return new LocalizedFormats("cannot format given object as a fraction number");
		   }
	public static LocalizedFormats CANNOT_INCREMENT_STATISTIC_CONSTRUCTED_FROM_EXTERNAL_MOMENTS() {
		   return new LocalizedFormats("statistics constructed from external moments cannot be incremented");
		   }
	public static LocalizedFormats CANNOT_NORMALIZE_A_ZERO_NORM_VECTOR() {
		   return new LocalizedFormats("cannot normalize a zero norm vector");
		   }
	public static LocalizedFormats CANNOT_RETRIEVE_AT_NEGATIVE_INDEX() {
		   return new LocalizedFormats("elements cannot be retrieved from a negative array index {0}");
		   }
	public static LocalizedFormats CANNOT_SET_AT_NEGATIVE_INDEX() {
		   return new LocalizedFormats("cannot set an element at a negative index {0}");
		   }
	public static LocalizedFormats CANNOT_SUBSTITUTE_ELEMENT_FROM_EMPTY_ARRAY() {
		   return new LocalizedFormats("cannot substitute an element from an empty array");
		   }
	public static LocalizedFormats CANNOT_TRANSFORM_TO_DOUBLE() {
		   return new LocalizedFormats("Conversion Exception in Transformation: {0}");
		   }
	public static LocalizedFormats CARDAN_ANGLES_SINGULARITY() {
		   return new LocalizedFormats("Cardan angles singularity");
		   }
	public static LocalizedFormats CLASS_DOESNT_IMPLEMENT_COMPARABLE() {
		   return new LocalizedFormats("class ({0}) does not implement Comparable");
		   }
	public static LocalizedFormats CLOSE_VERTICES() {
		   return new LocalizedFormats("too close vertices near point ({0}, {1}, {2})");
		   }
	public static LocalizedFormats CLOSEST_ORTHOGONAL_MATRIX_HAS_NEGATIVE_DETERMINANT() {
		   return new LocalizedFormats("the closest orthogonal matrix has a negative determinant {0}");
		   }
	public static LocalizedFormats COLUMN_INDEX_OUT_OF_RANGE() {
		   return new LocalizedFormats("column index {0} out of allowed range [{1}, {2}]");
		   }
	public static LocalizedFormats COLUMN_INDEX() {
		   return new LocalizedFormats("column index ({0})") /* keep */;
		   }
	public static LocalizedFormats CONSTRAINT() {
		   return new LocalizedFormats("constraint") /* keep */;
		   }
	public static LocalizedFormats CONTINUED_FRACTION_INFINITY_DIVERGENCE() {
		   return new LocalizedFormats("Continued fraction convergents diverged to +/- infinity for value {0}");
		   }
	public static LocalizedFormats CONTINUED_FRACTION_NAN_DIVERGENCE() {
		   return new LocalizedFormats("Continued fraction diverged to NaN for value {0}");
		   }
	public static LocalizedFormats CONTRACTION_CRITERIA_SMALLER_THAN_EXPANSION_FACTOR() {
		   return new LocalizedFormats("contraction criteria ({0}) smaller than the expansion factor ({1}).  This would lead to a never ending loop of expansion and contraction as a newly expanded internal storage array would immediately satisfy the criteria for contraction.");
		   }
	public static LocalizedFormats CONTRACTION_CRITERIA_SMALLER_THAN_ONE() {
		   return new LocalizedFormats("contraction criteria smaller than one ({0}).  This would lead to a never ending loop of expansion and contraction as an internal storage array length equal to the number of elements would satisfy the contraction criteria.");
		   }
	public static LocalizedFormats CONVERGENCE_FAILED() {
		   return new LocalizedFormats("convergence failed") /* keep */;
		   }
	public static LocalizedFormats CROSSING_BOUNDARY_LOOPS() {
		   return new LocalizedFormats("some outline boundary loops cross each other");
		   }
	public static LocalizedFormats CROSSOVER_RATE() {
		   return new LocalizedFormats("crossover rate ({0})");
		   }
	public static LocalizedFormats CUMULATIVE_PROBABILITY_RETURNED_NAN() {
		   return new LocalizedFormats("Cumulative probability function returned NaN for argument {0} p = {1}");
		   }
	public static LocalizedFormats DIFFERENT_ROWS_LENGTHS() {
		   return new LocalizedFormats("some rows have length {0} while others have length {1}");
		   }
	public static LocalizedFormats DIFFERENT_ORIG_AND_PERMUTED_DATA() {
		   return new LocalizedFormats("original and permuted data must contain the same elements");
		   }
	public static LocalizedFormats DIGEST_NOT_INITIALIZED() {
		   return new LocalizedFormats("digest not initialized");
		   }
	public static LocalizedFormats DIMENSIONS_MISMATCH_2x2() {
		   return new LocalizedFormats("got {0}x{1} but expected {2}x{3}") /* keep */;
		   }
	public static LocalizedFormats DIMENSIONS_MISMATCH_SIMPLE() {
		   return new LocalizedFormats("{0} != {1}") /* keep */;
		   }
	public static LocalizedFormats DIMENSIONS_MISMATCH() {
		   return new LocalizedFormats("dimensions mismatch") /* keep */;
		   }
	public static LocalizedFormats DISCRETE_CUMULATIVE_PROBABILITY_RETURNED_NAN() {
		   return new LocalizedFormats("Discrete cumulative probability function returned NaN for argument {0}");
		   }
	public static LocalizedFormats DISTRIBUTION_NOT_LOADED() {
		   return new LocalizedFormats("distribution not loaded");
		   }
	public static LocalizedFormats DUPLICATED_ABSCISSA_DIVISION_BY_ZERO() {
		   return new LocalizedFormats("duplicated abscissa {0} causes division by zero");
		   }
	public static LocalizedFormats EDGE_CONNECTED_TO_ONE_FACET() {
		   return new LocalizedFormats("edge joining points ({0}, {1}, {2}) and ({3}, {4}, {5}) is connected to one facet only");
		   }
	public static LocalizedFormats ELITISM_RATE() {
		   return new LocalizedFormats("elitism rate ({0})");
		   }
	public static LocalizedFormats EMPTY_CLUSTER_IN_K_MEANS() {
		   return new LocalizedFormats("empty cluster in k-means");
		   }
	public static LocalizedFormats EMPTY_INTERPOLATION_SAMPLE() {
		   return new LocalizedFormats("sample for interpolation is empty");
		   }
	public static LocalizedFormats EMPTY_POLYNOMIALS_COEFFICIENTS_ARRAY() {
		   return new LocalizedFormats("empty polynomials coefficients array") /* keep */;
		   }
	public static LocalizedFormats EMPTY_SELECTED_COLUMN_INDEX_ARRAY() {
		   return new LocalizedFormats("empty selected column index array");
		   }
	public static LocalizedFormats EMPTY_SELECTED_ROW_INDEX_ARRAY() {
		   return new LocalizedFormats("empty selected row index array");
		   }
	public static LocalizedFormats EMPTY_STRING_FOR_IMAGINARY_CHARACTER() {
		   return new LocalizedFormats("empty string for imaginary character");
		   }
	public static LocalizedFormats ENDPOINTS_NOT_AN_INTERVAL() {
		   return new LocalizedFormats("endpoints do not specify an interval: [{0}, {1}]");
		   }
	public static LocalizedFormats EQUAL_VERTICES_IN_SIMPLEX() {
		   return new LocalizedFormats("equal vertices {0} and {1} in simplex configuration");
		   }
	public static LocalizedFormats EULER_ANGLES_SINGULARITY() {
		   return new LocalizedFormats("Euler angles singularity");
		   }
	public static LocalizedFormats EVALUATION() {
		   return new LocalizedFormats("evaluation") /* keep */;
		   }
	public static LocalizedFormats EXPANSION_FACTOR_SMALLER_THAN_ONE() {
		   return new LocalizedFormats("expansion factor smaller than one ({0})");
		   }
	public static LocalizedFormats FACET_ORIENTATION_MISMATCH() {
		   return new LocalizedFormats("facets orientation mismatch around edge joining points ({0}, {1}, {2}) and ({3}, {4}, {5})");
		   }
	public static LocalizedFormats FACTORIAL_NEGATIVE_PARAMETER() {
		   return new LocalizedFormats("must have n >= 0 for n!, got n = {0}");
		   }
	public static LocalizedFormats FAILED_BRACKETING() {
		   return new LocalizedFormats("number of iterations={4}, maximum iterations={5}, initial={6}, lower bound={7}, upper bound={8}, final a value={0}, final b value={1}, f(a)={2}, f(b)={3}");
		   }
	public static LocalizedFormats FAILED_FRACTION_CONVERSION() {
		   return new LocalizedFormats("Unable to convert {0} to fraction after {1} iterations");
		   }
	public static LocalizedFormats FIRST_COLUMNS_NOT_INITIALIZED_YET() {
		   return new LocalizedFormats("first {0} columns are not initialized yet");
		   }
	public static LocalizedFormats FIRST_ELEMENT_NOT_ZERO() {
		   return new LocalizedFormats("first element is not 0: {0}");
		   }
	public static LocalizedFormats FIRST_ROWS_NOT_INITIALIZED_YET() {
		   return new LocalizedFormats("first {0} rows are not initialized yet");
		   }
	public static LocalizedFormats FRACTION_CONVERSION_OVERFLOW() {
		   return new LocalizedFormats("Overflow trying to convert {0} to fraction ({1}/{2})");
		   }
	public static LocalizedFormats FUNCTION_NOT_DIFFERENTIABLE() {
		   return new LocalizedFormats("function is not differentiable");
		   }
	public static LocalizedFormats FUNCTION_NOT_POLYNOMIAL() {
		   return new LocalizedFormats("function is not polynomial");
		   }
	public static LocalizedFormats GCD_OVERFLOW_32_BITS() {
		   return new LocalizedFormats("overflow: gcd({0}, {1}) is 2^31");
		   }
	public static LocalizedFormats GCD_OVERFLOW_64_BITS() {
		   return new LocalizedFormats("overflow: gcd({0}, {1}) is 2^63");
		   }
	public static LocalizedFormats HOLE_BETWEEN_MODELS_TIME_RANGES() {
		   return new LocalizedFormats("{0} wide hole between models time ranges");
		   }
	public static LocalizedFormats ILL_CONDITIONED_OPERATOR() {
		   return new LocalizedFormats("condition number {1} is too high ");
		   }
	public static LocalizedFormats INCONSISTENT_STATE_AT_2_PI_WRAPPING() {
		   return new LocalizedFormats("inconsistent state at 2\u03c0 wrapping");
		   }
	public static LocalizedFormats INDEX_LARGER_THAN_MAX() {
		   return new LocalizedFormats("the index specified: {0} is larger than the current maximal index {1}");
		   }
	public static LocalizedFormats INDEX_NOT_POSITIVE() {
		   return new LocalizedFormats("index ({0}) is not positive");
		   }
	public static LocalizedFormats INDEX_OUT_OF_RANGE() {
		   return new LocalizedFormats("index {0} out of allowed range [{1}, {2}]");
		   }
	public static LocalizedFormats INDEX() {
		   return new LocalizedFormats("index ({0})") /* keep */;
		   }
	public static LocalizedFormats NOT_FINITE_NUMBER() {
		   return new LocalizedFormats("{0} is not a finite number") /* keep */;
		   }
	public static LocalizedFormats INFINITE_BOUND() {
		   return new LocalizedFormats("interval bounds must be finite");
		   }
	public static LocalizedFormats ARRAY_ELEMENT() {
		   return new LocalizedFormats("value {0} at index {1}") /* keep */;
		   }
	public static LocalizedFormats INFINITE_ARRAY_ELEMENT() {
		   return new LocalizedFormats("Array contains an infinite element, {0} at index {1}");
		   }
	public static LocalizedFormats INFINITE_VALUE_CONVERSION() {
		   return new LocalizedFormats("cannot convert infinite value");
		   }
	public static LocalizedFormats INITIAL_CAPACITY_NOT_POSITIVE() {
		   return new LocalizedFormats("initial capacity ({0}) is not positive");
		   }
	public static LocalizedFormats INITIAL_COLUMN_AFTER_FINAL_COLUMN() {
		   return new LocalizedFormats("initial column {1} after final column {0}");
		   }
	public static LocalizedFormats INITIAL_ROW_AFTER_FINAL_ROW() {
		   return new LocalizedFormats("initial row {1} after final row {0}");
		   }
	@Deprecated
	public static LocalizedFormats INPUT_DATA_FROM_UNSUPPORTED_DATASOURCE() {
		return new LocalizedFormats("input data comes from unsupported datasource: {0}, supported sources: {1}, {2}");
	}
	public static LocalizedFormats INSTANCES_NOT_COMPARABLE_TO_EXISTING_VALUES() {
		   return new LocalizedFormats("instance of class {0} not comparable to existing values");
		   }
	public static LocalizedFormats INSUFFICIENT_DATA() {
		   return new LocalizedFormats("insufficient data");
		   }
	public static LocalizedFormats INSUFFICIENT_DATA_FOR_T_STATISTIC() {
		   return new LocalizedFormats("insufficient data for t statistic, needs at least 2, got {0}");
		   }
	
	public static LocalizedFormats INSUFFICIENT_DIMENSION() {
		return new LocalizedFormats("insufficient dimension {0}, must be at least {1}") /* keep */;
	}
	public static LocalizedFormats INSUFFICIENT_OBSERVED_POINTS_IN_SAMPLE() {
		   return new LocalizedFormats("sample contains {0} observed points, at least {1} are required");
		   }
	public static LocalizedFormats INSUFFICIENT_ROWS_AND_COLUMNS() {
		   return new LocalizedFormats("insufficient data: only {0} rows and {1} columns.");
		   }
	public static LocalizedFormats INTEGRATION_METHOD_NEEDS_AT_LEAST_TWO_PREVIOUS_POINTS() {
		   return new LocalizedFormats("multistep method needs at least {0} previous steps, got {1}");
		   }
	public static LocalizedFormats INTERNAL_ERROR() {
		   return new LocalizedFormats("internal error, please fill a bug report at {0}");
		   }
	public static LocalizedFormats INVALID_BINARY_DIGIT() {
		   return new LocalizedFormats("invalid binary digit: {0}");
		   }
	public static LocalizedFormats INVALID_BINARY_CHROMOSOME() {
		   return new LocalizedFormats("binary mutation works on BinaryChromosome only");
		   }
	public static LocalizedFormats INVALID_BRACKETING_PARAMETERS() {
		   return new LocalizedFormats("invalid bracketing parameters:  lower bound={0},  initial={1}, upper bound={2}");
		   }
	public static LocalizedFormats INVALID_FIXED_LENGTH_CHROMOSOME() {
		   return new LocalizedFormats("one-point crossover only works with fixed-length chromosomes");
		   }
	public static LocalizedFormats INVALID_IMPLEMENTATION() {
		   return new LocalizedFormats("required functionality is missing in {0}");
		   }
	public static LocalizedFormats INVALID_INTERVAL_INITIAL_VALUE_PARAMETERS() {
		   return new LocalizedFormats("invalid interval, initial value parameters:  lower={0}, initial={1}, upper={2}");
		   }
	public static LocalizedFormats INVALID_ITERATIONS_LIMITS() {
		   return new LocalizedFormats("invalid iteration limits: min={0}, max={1}");
		   }
	public static LocalizedFormats INVALID_MAX_ITERATIONS() {
		return new LocalizedFormats("bad value for maximum iterations number: {0}");
	}
	public static LocalizedFormats NOT_ENOUGH_DATA_REGRESSION() {
		return new LocalizedFormats("the number of observations is not sufficient to conduct regression");
	}
	public static LocalizedFormats INVALID_REGRESSION_ARRAY() {
		   return new LocalizedFormats("input data array length = {0} does not match the number of observations = {1} and the number of regressors = {2}");
		   }
	public static LocalizedFormats INVALID_REGRESSION_OBSERVATION() {
		   return new LocalizedFormats("length of regressor array = {0} does not match the number of variables = {1} in the model");
		   }
	public static LocalizedFormats INVALID_ROUNDING_METHOD() {
		   return new LocalizedFormats("invalid rounding method {0}, valid methods: {1} ({2}), {3} ({4}), {5} ({6}), {7} ({8}), {9} ({10}), {11} ({12}), {13} ({14}), {15} ({16})");
		   }
	public static LocalizedFormats ITERATOR_EXHAUSTED() {
		   return new LocalizedFormats("iterator exhausted");
		   }
	public static LocalizedFormats ITERATIONS() {
		   return new LocalizedFormats("iterations") /* keep */;
		   }
	public static LocalizedFormats LCM_OVERFLOW_32_BITS() {
		   return new LocalizedFormats("overflow: lcm({0}, {1}) is 2^31");
		   }
	public static LocalizedFormats LCM_OVERFLOW_64_BITS() {
		   return new LocalizedFormats("overflow: lcm({0}, {1}) is 2^63");
		   }
	public static LocalizedFormats LIST_OF_CHROMOSOMES_BIGGER_THAN_POPULATION_SIZE() {
		   return new LocalizedFormats("list of chromosomes bigger than maxPopulationSize");
		   }
	public static LocalizedFormats LOESS_EXPECTS_AT_LEAST_ONE_POINT() {
		   return new LocalizedFormats("Loess expects at least 1 point");
		   }
	public static LocalizedFormats LOWER_BOUND_NOT_BELOW_UPPER_BOUND() {
		   return new LocalizedFormats("lower bound ({0}) must be strictly less than upper bound ({1})") /* keep */;
		   }
	public static LocalizedFormats LOWER_ENDPOINT_ABOVE_UPPER_ENDPOINT() {
		   return new LocalizedFormats("lower endpoint ({0}) must be less than or equal to upper endpoint ({1})");
		   }
	public static LocalizedFormats MAP_MODIFIED_WHILE_ITERATING() {
		return new LocalizedFormats("map has been modified while iterating");
	}
	public static LocalizedFormats MULTISTEP_STARTER_STOPPED_EARLY() {
		   return new LocalizedFormats("multistep integrator starter stopped early, maybe too large step size");
		   }
	public static LocalizedFormats EVALUATIONS() {
		   return new LocalizedFormats("evaluations") /* keep */;
		   }
	public static LocalizedFormats MAX_COUNT_EXCEEDED() {
		   return new LocalizedFormats("maximal count ({0}) exceeded") /* keep */;
		   }
	public static LocalizedFormats MAX_ITERATIONS_EXCEEDED() {
		   return new LocalizedFormats("maximal number of iterations ({0}) exceeded");
		   }
	public static LocalizedFormats MINIMAL_STEPSIZE_REACHED_DURING_INTEGRATION() {
		   return new LocalizedFormats("minimal step size ({1,number,0.00E00}) reached, integration needs {0,number,0.00E00}");
		   }
	public static LocalizedFormats MISMATCHED_LOESS_ABSCISSA_ORDINATE_ARRAYS() {
		   return new LocalizedFormats("Loess expects the abscissa and ordinate arrays to be of the same size, but got {0} abscissae and {1} ordinatae");
		   }
	public static LocalizedFormats MUTATION_RATE() {
		   return new LocalizedFormats("mutation rate ({0})");
		   }
	public static LocalizedFormats NAN_ELEMENT_AT_INDEX() {
		   return new LocalizedFormats("element {0} is NaN");
		   }
	public static LocalizedFormats NAN_VALUE_CONVERSION() {
		   return new LocalizedFormats("cannot convert NaN value");
		   }
	public static LocalizedFormats NEGATIVE_BRIGHTNESS_EXPONENT() {
		   return new LocalizedFormats("brightness exponent should be positive or null, but got {0}");
		   }
	public static LocalizedFormats NEGATIVE_COMPLEX_MODULE() {
		   return new LocalizedFormats("negative complex module {0}");
		   }
	public static LocalizedFormats NEGATIVE_ELEMENT_AT_2D_INDEX() {
		   return new LocalizedFormats("element ({0}, {1}) is negative: {2}");
		   }
	public static LocalizedFormats NEGATIVE_ELEMENT_AT_INDEX() {
		   return new LocalizedFormats("element {0} is negative: {1}");
		   }
	public static LocalizedFormats NEGATIVE_NUMBER_OF_SUCCESSES() {
		   return new LocalizedFormats("number of successes must be non-negative ({0})");
		   }
	public static LocalizedFormats NUMBER_OF_SUCCESSES() {
		   return new LocalizedFormats("number of successes ({0})") /* keep */;
		   }
	public static LocalizedFormats NEGATIVE_NUMBER_OF_TRIALS() {
		   return new LocalizedFormats("number of trials must be non-negative ({0})");
		   }
	public static LocalizedFormats NUMBER_OF_INTERPOLATION_POINTS() {
		   return new LocalizedFormats("number of interpolation points ({0})") /* keep */;
		   }
	public static LocalizedFormats NUMBER_OF_TRIALS() {
		   return new LocalizedFormats("number of trials ({0})");
		   }
	public static LocalizedFormats NOT_CONVEX() {
		   return new LocalizedFormats("vertices do not form a convex hull in CCW winding");
		   }
	public static LocalizedFormats NOT_CONVEX_HYPERPLANES() {
		   return new LocalizedFormats("hyperplanes do not define a convex region");
		   }
	public static LocalizedFormats ROBUSTNESS_ITERATIONS() {
		   return new LocalizedFormats("number of robustness iterations ({0})");
		   }
	public static LocalizedFormats START_POSITION() {
		   return new LocalizedFormats("start position ({0})") /* keep */;
		   }
	public static LocalizedFormats NON_CONVERGENT_CONTINUED_FRACTION() {
		   return new LocalizedFormats("Continued fraction convergents failed to converge (in less than {0} iterations) for value {1}");
		   }
	public static LocalizedFormats NON_INVERTIBLE_TRANSFORM() {
		   return new LocalizedFormats("non-invertible affine transform collapses some lines into single points");
		   }
	public static LocalizedFormats NON_POSITIVE_MICROSPHERE_ELEMENTS() {
		   return new LocalizedFormats("number of microsphere elements must be positive, but got {0}");
		   }
	public static LocalizedFormats NON_POSITIVE_POLYNOMIAL_DEGREE() {
		   return new LocalizedFormats("polynomial degree must be positive: degree={0}");
		   }
	public static LocalizedFormats NON_REAL_FINITE_ABSCISSA() {
		   return new LocalizedFormats("all abscissae must be finite real numbers, but {0}-th is {1}");
		   }
	public static LocalizedFormats NON_REAL_FINITE_ORDINATE() {
		   return new LocalizedFormats("all ordinatae must be finite real numbers, but {0}-th is {1}");
		   }
	public static LocalizedFormats NON_REAL_FINITE_WEIGHT() {
		   return new LocalizedFormats("all weights must be finite real numbers, but {0}-th is {1}");
		   }
	public static LocalizedFormats NON_SQUARE_MATRIX() {
		   return new LocalizedFormats("non square ({0}x{1}) matrix");
		   }
	public static LocalizedFormats NORM() {
		   return new LocalizedFormats("Norm ({0})") /* keep */;
		   }
	public static LocalizedFormats NORMALIZE_INFINITE() {
		   return new LocalizedFormats("Cannot normalize to an infinite value");
		   }
	public static LocalizedFormats NORMALIZE_NAN() {
		   return new LocalizedFormats("Cannot normalize to NaN");
		   }
	public static LocalizedFormats NOT_ADDITION_COMPATIBLE_MATRICES() {
		   return new LocalizedFormats("{0}x{1} and {2}x{3} matrices are not addition compatible");
		   }
	public static LocalizedFormats NOT_DECREASING_NUMBER_OF_POINTS() {
		   return new LocalizedFormats("points {0} and {1} are not decreasing ({2} < {3})");
		   }
	public static LocalizedFormats NOT_DECREASING_SEQUENCE() {
		   return new LocalizedFormats("points {3} and {2} are not decreasing ({1} < {0})") /* keep */;
		   }
	public static LocalizedFormats NOT_ENOUGH_DATA_FOR_NUMBER_OF_PREDICTORS() {
		   return new LocalizedFormats("not enough data ({0} rows) for this many predictors ({1} predictors)");
		   }
	public static LocalizedFormats NOT_ENOUGH_POINTS_IN_SPLINE_PARTITION() {
		   return new LocalizedFormats("spline partition must have at least {0} points, got {1}");
		   }
	public static LocalizedFormats NOT_INCREASING_NUMBER_OF_POINTS() {
		   return new LocalizedFormats("points {0} and {1} are not increasing ({2} > {3})");
		   }
	public static LocalizedFormats NOT_INCREASING_SEQUENCE() {
		   return new LocalizedFormats("points {3} and {2} are not increasing ({1} > {0})") /* keep */;
		   }
	public static LocalizedFormats NOT_MULTIPLICATION_COMPATIBLE_MATRICES() {
		   return new LocalizedFormats("{0}x{1} and {2}x{3} matrices are not multiplication compatible");
		   }
	public static LocalizedFormats NOT_POSITIVE_DEFINITE_MATRIX() {
		   return new LocalizedFormats("not positive definite matrix") /* keep */;
		   }
	public static LocalizedFormats NON_POSITIVE_DEFINITE_MATRIX() {
		   return new LocalizedFormats("not positive definite matrix: diagonal element at ({1},{1}) is smaller than {2} ({0})");
		   }
	public static LocalizedFormats NON_POSITIVE_DEFINITE_OPERATOR() {
		   return new LocalizedFormats("non positive definite linear operator") /* keep */;
		   }
	public static LocalizedFormats NON_SELF_ADJOINT_OPERATOR() {
		   return new LocalizedFormats("non self-adjoint linear operator") /* keep */;
		   }
	public static LocalizedFormats NON_SQUARE_OPERATOR() {
		   return new LocalizedFormats("non square ({0}x{1}) linear operator") /* keep */;
		   }
	public static LocalizedFormats DEGREES_OF_FREEDOM() {
		   return new LocalizedFormats("degrees of freedom ({0})") /* keep */;
		   }
	public static LocalizedFormats NOT_POSITIVE_DEGREES_OF_FREEDOM() {
		   return new LocalizedFormats("degrees of freedom must be positive ({0})");
		   }
	public static LocalizedFormats NOT_POSITIVE_ELEMENT_AT_INDEX() {
		   return new LocalizedFormats("element {0} is not positive: {1}");
		   }
	public static LocalizedFormats NOT_POSITIVE_EXPONENT() {
		   return new LocalizedFormats("invalid exponent {0} (must be positive)");
		   }
	public static LocalizedFormats NUMBER_OF_ELEMENTS_SHOULD_BE_POSITIVE() {
		   return new LocalizedFormats("number of elements should be positive ({0})");
		   }
	public static LocalizedFormats BASE() {
		   return new LocalizedFormats("base ({0})") /* keep */;
		   }
	public static LocalizedFormats EXPONENT() {
		   return new LocalizedFormats("exponent ({0})") /* keep */;
		   }
	public static LocalizedFormats NOT_POSITIVE_LENGTH() {
		   return new LocalizedFormats("length must be positive ({0})");
		   }
	public static LocalizedFormats LENGTH() {
		   return new LocalizedFormats("length ({0})") /* keep */;
		   }
	public static LocalizedFormats NOT_POSITIVE_MEAN() {
		   return new LocalizedFormats("mean must be positive ({0})");
		   }
	public static LocalizedFormats MEAN() {
		   return new LocalizedFormats("mean ({0})") /* keep */;
		   }
	public static LocalizedFormats NOT_POSITIVE_NUMBER_OF_SAMPLES() {
		   return new LocalizedFormats("number of sample is not positive: {0}");
		   }
	public static LocalizedFormats NUMBER_OF_SAMPLES() {
		   return new LocalizedFormats("number of samples ({0})") /* keep */;
		   }
	public static LocalizedFormats NOT_POSITIVE_PERMUTATION() {
		   return new LocalizedFormats("permutation k ({0}) must be positive");
		   }
	public static LocalizedFormats PERMUTATION_SIZE() {
		   return new LocalizedFormats("permutation size ({0}") /* keep */;
		   }
	public static LocalizedFormats NOT_POSITIVE_POISSON_MEAN() {
		   return new LocalizedFormats("the Poisson mean must be positive ({0})");
		   }
	public static LocalizedFormats NOT_POSITIVE_POPULATION_SIZE() {
		   return new LocalizedFormats("population size must be positive ({0})");
		   }
	public static LocalizedFormats POPULATION_SIZE() {
		   return new LocalizedFormats("population size ({0})") /* keep */;
		   }
	public static LocalizedFormats NOT_POSITIVE_ROW_DIMENSION() {
		   return new LocalizedFormats("invalid row dimension: {0} (must be positive)");
		   }
	public static LocalizedFormats NOT_POSITIVE_SAMPLE_SIZE() {
		   return new LocalizedFormats("sample size must be positive ({0})");
		   }
	public static LocalizedFormats NOT_POSITIVE_SCALE() {
		   return new LocalizedFormats("scale must be positive ({0})");
		   }
	public static LocalizedFormats SCALE() {
		   return new LocalizedFormats("scale ({0})") /* keep */;
		   }
	public static LocalizedFormats NOT_POSITIVE_SHAPE() {
		   return new LocalizedFormats("shape must be positive ({0})");
		   }
	public static LocalizedFormats SHAPE() {
		   return new LocalizedFormats("shape ({0})") /* keep */;
		   }
	public static LocalizedFormats NOT_POSITIVE_STANDARD_DEVIATION() {
		   return new LocalizedFormats("standard deviation must be positive ({0})");
		   }
	public static LocalizedFormats STANDARD_DEVIATION() {
		   return new LocalizedFormats("standard deviation ({0})") /* keep */;
		   }
	public static LocalizedFormats NOT_POSITIVE_UPPER_BOUND() {
		   return new LocalizedFormats("upper bound must be positive ({0})");
		   }
	public static LocalizedFormats NOT_POSITIVE_WINDOW_SIZE() {
		   return new LocalizedFormats("window size must be positive ({0})");
		   }
	public static LocalizedFormats NOT_POWER_OF_TWO() {
		   return new LocalizedFormats("{0} is not a power of 2");
		   }
	public static LocalizedFormats NOT_POWER_OF_TWO_CONSIDER_PADDING() {
		   return new LocalizedFormats("{0} is not a power of 2, consider padding for fix");
		   }
	public static LocalizedFormats NOT_POWER_OF_TWO_PLUS_ONE() {
		   return new LocalizedFormats("{0} is not a power of 2 plus one");
		   }
	public static LocalizedFormats NOT_STRICTLY_DECREASING_NUMBER_OF_POINTS() {
		   return new LocalizedFormats("points {0} and {1} are not strictly decreasing ({2} <= {3})");
		   }
	public static LocalizedFormats NOT_STRICTLY_DECREASING_SEQUENCE() {
		   return new LocalizedFormats("points {3} and {2} are not strictly decreasing ({1} <= {0})") /* keep */;
		   }
	public static LocalizedFormats NOT_STRICTLY_INCREASING_KNOT_VALUES() {
		   return new LocalizedFormats("knot values must be strictly increasing");
		   }
	public static LocalizedFormats NOT_STRICTLY_INCREASING_NUMBER_OF_POINTS() {
		   return new LocalizedFormats("points {0} and {1} are not strictly increasing ({2} >= {3})");
		   }
	public static LocalizedFormats NOT_STRICTLY_INCREASING_SEQUENCE() {
		   return new LocalizedFormats("points {3} and {2} are not strictly increasing ({1} >= {0})") /* keep */;
		   }
	public static LocalizedFormats NOT_SUBTRACTION_COMPATIBLE_MATRICES() {
		   return new LocalizedFormats("{0}x{1} and {2}x{3} matrices are not subtraction compatible");
		   }
	public static LocalizedFormats NOT_SUPPORTED_IN_DIMENSION_N() {
		   return new LocalizedFormats("method not supported in dimension {0}");
		   }
	public static LocalizedFormats NOT_SYMMETRIC_MATRIX() {
		   return new LocalizedFormats("not symmetric matrix");
		   }
	public static LocalizedFormats NON_SYMMETRIC_MATRIX() {
		   return new LocalizedFormats("non symmetric matrix: the difference between entries at ({0},{1}) and ({1},{0}) is larger than {2}") /* keep */;
		   }
	public static LocalizedFormats NO_BIN_SELECTED() {
		   return new LocalizedFormats("no bin selected");
		   }
	public static LocalizedFormats NO_CONVERGENCE_WITH_ANY_START_POINT() {
		   return new LocalizedFormats("none of the {0} start points lead to convergence") /* keep */;
		   }
	public static LocalizedFormats NO_DATA() {
		   return new LocalizedFormats("no data") /* keep */;
		   }
	public static LocalizedFormats NO_DEGREES_OF_FREEDOM() {
		   return new LocalizedFormats("no degrees of freedom ({0} measurements, {1} parameters)");
		   }
	public static LocalizedFormats NO_DENSITY_FOR_THIS_DISTRIBUTION() {
		   return new LocalizedFormats("This distribution does not have a density function implemented");
		   }
	public static LocalizedFormats NO_FEASIBLE_SOLUTION() {
		   return new LocalizedFormats("no feasible solution");
		   }
	public static LocalizedFormats NO_OPTIMUM_COMPUTED_YET() {
		   return new LocalizedFormats("no optimum computed yet") /* keep */;
		   }
	public static LocalizedFormats NO_REGRESSORS() {
		   return new LocalizedFormats("Regression model must include at least one regressor");
		   }
	public static LocalizedFormats NO_RESULT_AVAILABLE() {
		   return new LocalizedFormats("no result available");
		   }
	public static LocalizedFormats NO_SUCH_MATRIX_ENTRY() {
		   return new LocalizedFormats("no entry at indices ({0}, {1}) in a {2}x{3} matrix");
		   }
	public static LocalizedFormats NAN_NOT_ALLOWED() {
		   return new LocalizedFormats("NaN is not allowed");
		   }
	public static LocalizedFormats NULL_NOT_ALLOWED() {
		   return new LocalizedFormats("null is not allowed") /* keep */;
		   }
	public static LocalizedFormats ARRAY_ZERO_LENGTH_OR_NULL_NOT_ALLOWED() {
		   return new LocalizedFormats("a null or zero length array not allowed");
		   }
	public static LocalizedFormats COVARIANCE_MATRIX() {
		   return new LocalizedFormats("covariance matrix") /* keep */;
		   }
	public static LocalizedFormats DENOMINATOR() {
		   return new LocalizedFormats("denominator") /* keep */;
		   }
	public static LocalizedFormats DENOMINATOR_FORMAT() {
		   return new LocalizedFormats("denominator format") /* keep */;
		   }
	public static LocalizedFormats FRACTION() {
		   return new LocalizedFormats("fraction") /* keep */;
		   }
	public static LocalizedFormats FUNCTION() {
		   return new LocalizedFormats("function") /* keep */;
		   }
	public static LocalizedFormats IMAGINARY_FORMAT() {
		   return new LocalizedFormats("imaginary format") /* keep */;
		   }
	public static LocalizedFormats INPUT_ARRAY() {
		   return new LocalizedFormats("input array") /* keep */;
		   }
	public static LocalizedFormats NUMERATOR() {
		   return new LocalizedFormats("numerator") /* keep */;
		   }
	public static LocalizedFormats NUMERATOR_FORMAT() {
		   return new LocalizedFormats("numerator format") /* keep */;
		   }
	public static LocalizedFormats OBJECT_TRANSFORMATION() {
		   return new LocalizedFormats("conversion exception in transformation") /* keep */;
		   }
	public static LocalizedFormats REAL_FORMAT() {
		   return new LocalizedFormats("real format") /* keep */;
		   }
	public static LocalizedFormats WHOLE_FORMAT() {
		   return new LocalizedFormats("whole format") /* keep */;
		   }
	public static LocalizedFormats NUMBER_TOO_LARGE() {
		   return new LocalizedFormats("{0} is larger than the maximum ({1})") /* keep */;
		   }
	public static LocalizedFormats NUMBER_TOO_SMALL() {
		   return new LocalizedFormats("{0} is smaller than the minimum ({1})") /* keep */;
		   }
	public static LocalizedFormats NUMBER_TOO_LARGE_BOUND_EXCLUDED() {
		   return new LocalizedFormats("{0} is larger than, or equal to, the maximum ({1})") /* keep */;
		   }
	public static LocalizedFormats NUMBER_TOO_SMALL_BOUND_EXCLUDED() {
		   return new LocalizedFormats("{0} is smaller than, or equal to, the minimum ({1})") /* keep */;
		   }
	public static LocalizedFormats NUMBER_OF_SUCCESS_LARGER_THAN_POPULATION_SIZE() {
		   return new LocalizedFormats("number of successes ({0}) must be less than or equal to population size ({1})");
		   }
	public static LocalizedFormats NUMERATOR_OVERFLOW_AFTER_MULTIPLY() {
		   return new LocalizedFormats("overflow, numerator too large after multiply: {0}");
		   }
	public static LocalizedFormats N_POINTS_GAUSS_LEGENDRE_INTEGRATOR_NOT_SUPPORTED() {
		   return new LocalizedFormats("{0} points Legendre-Gauss integrator not supported, number of points must be in the {1}-{2} range");
		   }
	public static LocalizedFormats OBSERVED_COUNTS_ALL_ZERO() {
		   return new LocalizedFormats("observed counts are all 0 in observed array {0}");
		   }
	public static LocalizedFormats OBSERVED_COUNTS_BOTTH_ZERO_FOR_ENTRY() {
		   return new LocalizedFormats("observed counts are both zero for entry {0}");
		   }
	public static LocalizedFormats BOBYQA_BOUND_DIFFERENCE_CONDITION() {
		   return new LocalizedFormats("the difference between the upper and lower bound must be larger than twice the initial trust region radius ({0})");
		   }
	public static LocalizedFormats OUT_OF_BOUNDS_QUANTILE_VALUE() {
		   return new LocalizedFormats("out of bounds quantile value: {0}, must be in (0, 100]");
		   }
	public static LocalizedFormats OUT_OF_BOUNDS_CONFIDENCE_LEVEL() {
		   return new LocalizedFormats("out of bounds confidence level {0}, must be between {1} and {2}");
		   }
	public static LocalizedFormats OUT_OF_BOUND_SIGNIFICANCE_LEVEL() {
		   return new LocalizedFormats("out of bounds significance level {0}, must be between {1} and {2}");
		   }
	public static LocalizedFormats SIGNIFICANCE_LEVEL() {
		   return new LocalizedFormats("significance level ({0})") /* keep */;
		   }
	public static LocalizedFormats OUT_OF_ORDER_ABSCISSA_ARRAY() {
		   return new LocalizedFormats("the abscissae array must be sorted in a strictly increasing order, but the {0}-th element is {1} whereas {2}-th is {3}");
		   }
	public static LocalizedFormats OUT_OF_PLANE() {
		   return new LocalizedFormats("point ({0}, {1}, {2}) is out of plane");
		   }
	public static LocalizedFormats OUT_OF_RANGE_ROOT_OF_UNITY_INDEX() {
		   return new LocalizedFormats("out of range root of unity index {0} (must be in [{1};{2}])");
		   }
	public static LocalizedFormats OUT_OF_RANGE() {
		   return new LocalizedFormats("out of range") /* keep */;
		   }
	public static LocalizedFormats OUT_OF_RANGE_SIMPLE() {
		   return new LocalizedFormats("{0} out of [{1}, {2}] range") /* keep */;
		   }
	public static LocalizedFormats OUT_OF_RANGE_LEFT() {
		   return new LocalizedFormats("{0} out of ({1}, {2}] range");
		   }
	public static LocalizedFormats OUT_OF_RANGE_RIGHT() {
		   return new LocalizedFormats("{0} out of [{1}, {2}) range");
		   }
	public static LocalizedFormats OUTLINE_BOUNDARY_LOOP_OPEN() {
		   return new LocalizedFormats("an outline boundary loop is open");
		   }
	public static LocalizedFormats OVERFLOW() {
		   return new LocalizedFormats("overflow") /* keep */;
		   }
	public static LocalizedFormats OVERFLOW_IN_FRACTION() {
		   return new LocalizedFormats("overflow in fraction {0}/{1}, cannot negate");
		   }
	public static LocalizedFormats OVERFLOW_IN_ADDITION() {
		   return new LocalizedFormats("overflow in addition: {0} + {1}");
		   }
	public static LocalizedFormats OVERFLOW_IN_SUBTRACTION() {
		   return new LocalizedFormats("overflow in subtraction: {0} - {1}");
		   }
	public static LocalizedFormats OVERFLOW_IN_MULTIPLICATION() {
		   return new LocalizedFormats("overflow in multiplication: {0} * {1}");
		   }
	public static LocalizedFormats PERCENTILE_IMPLEMENTATION_CANNOT_ACCESS_METHOD() {
		   return new LocalizedFormats("cannot access {0} method in percentile implementation {1}");
		   }
	public static LocalizedFormats PERCENTILE_IMPLEMENTATION_UNSUPPORTED_METHOD() {
		   return new LocalizedFormats("percentile implementation {0} does not support {1}");
		   }
	public static LocalizedFormats PERMUTATION_EXCEEDS_N() {
		   return new LocalizedFormats("permutation size ({0}) exceeds permuation domain ({1})") /* keep */;
		   }
	public static LocalizedFormats POLYNOMIAL() {
		   return new LocalizedFormats("polynomial") /* keep */;
		   }
	public static LocalizedFormats POLYNOMIAL_INTERPOLANTS_MISMATCH_SEGMENTS() {
		   return new LocalizedFormats("number of polynomial interpolants must match the number of segments ({0} != {1} - 1)");
		   }
	public static LocalizedFormats POPULATION_LIMIT_NOT_POSITIVE() {
		   return new LocalizedFormats("population limit has to be positive");
		   }
	public static LocalizedFormats POWER_NEGATIVE_PARAMETERS() {
		   return new LocalizedFormats("cannot raise an integral value to a negative power ({0}^{1})");
		   }
	public static LocalizedFormats PROPAGATION_DIRECTION_MISMATCH() {
		   return new LocalizedFormats("propagation direction mismatch");
		   }
	public static LocalizedFormats RANDOMKEY_MUTATION_WRONG_CLASS() {
		   return new LocalizedFormats("RandomKeyMutation works only with RandomKeys, not {0}");
		   }
	public static LocalizedFormats ROOTS_OF_UNITY_NOT_COMPUTED_YET() {
		   return new LocalizedFormats("roots of unity have not been computed yet");
		   }
	public static LocalizedFormats ROTATION_MATRIX_DIMENSIONS() {
		   return new LocalizedFormats("a {0}x{1} matrix cannot be a rotation matrix");
		   }
	public static LocalizedFormats ROW_INDEX_OUT_OF_RANGE() {
		   return new LocalizedFormats("row index {0} out of allowed range [{1}, {2}]");
		   }
	public static LocalizedFormats ROW_INDEX() {
		   return new LocalizedFormats("row index ({0})") /* keep */;
		   }
	public static LocalizedFormats SAME_SIGN_AT_ENDPOINTS() {
		   return new LocalizedFormats("function values at endpoints do not have different signs, endpoints: [{0}, {1}], values: [{2}, {3}]");
		   }
	public static LocalizedFormats SAMPLE_SIZE_EXCEEDS_COLLECTION_SIZE() {
		   return new LocalizedFormats("sample size ({0}) exceeds collection size ({1})") /* keep */;
		   }
	public static LocalizedFormats SAMPLE_SIZE_LARGER_THAN_POPULATION_SIZE() {
		   return new LocalizedFormats("sample size ({0}) must be less than or equal to population size ({1})");
		   }
	public static LocalizedFormats SIMPLEX_NEED_ONE_POINT() {
		   return new LocalizedFormats("simplex must contain at least one point");
		   }
	public static LocalizedFormats SIMPLE_MESSAGE() {
		   return new LocalizedFormats("{0}");
		   }
	public static LocalizedFormats SINGULAR_MATRIX() {
		   return new LocalizedFormats("matrix is singular") /* keep */;
		   }
	public static LocalizedFormats SINGULAR_OPERATOR() {
		   return new LocalizedFormats("operator is singular");
		   }
	public static LocalizedFormats SUBARRAY_ENDS_AFTER_ARRAY_END() {
		   return new LocalizedFormats("subarray ends after array end");
		   }
	public static LocalizedFormats TOO_LARGE_CUTOFF_SINGULAR_VALUE() {
		   return new LocalizedFormats("cutoff singular value is {0}, should be at most {1}");
		   }
	public static LocalizedFormats TOO_LARGE_TOURNAMENT_ARITY() {
		   return new LocalizedFormats("tournament arity ({0}) cannot be bigger than population size ({1})");
		   }
	public static LocalizedFormats TOO_MANY_ELEMENTS_TO_DISCARD_FROM_ARRAY() {
		   return new LocalizedFormats("cannot discard {0} elements from a {1} elements array");
		   }
	public static LocalizedFormats TOO_MANY_REGRESSORS() {
		   return new LocalizedFormats("too many regressors ({0}) specified, only {1} in the model");
		   }
	public static LocalizedFormats TOO_SMALL_COST_RELATIVE_TOLERANCE() {
		   return new LocalizedFormats("cost relative tolerance is too small ({0}), no further reduction in the sum of squares is possible");
		   }
	public static LocalizedFormats TOO_SMALL_INTEGRATION_INTERVAL() {
		   return new LocalizedFormats("too small integration interval: length = {0}");
		   }
	public static LocalizedFormats TOO_SMALL_ORTHOGONALITY_TOLERANCE() {
		   return new LocalizedFormats("orthogonality tolerance is too small ({0}), solution is orthogonal to the jacobian");
		   }
	public static LocalizedFormats TOO_SMALL_PARAMETERS_RELATIVE_TOLERANCE() {
		   return new LocalizedFormats("parameters relative tolerance is too small ({0}), no further improvement in the approximate solution is possible");
		   }
	public static LocalizedFormats TRUST_REGION_STEP_FAILED() {
		   return new LocalizedFormats("trust region step has failed to reduce Q");
		   }
	public static LocalizedFormats TWO_OR_MORE_CATEGORIES_REQUIRED() {
		   return new LocalizedFormats("two or more categories required, got {0}");
		   }
	public static LocalizedFormats TWO_OR_MORE_VALUES_IN_CATEGORY_REQUIRED() {
		   return new LocalizedFormats("two or more values required in each category, one has {0}");
		   }
	public static LocalizedFormats UNABLE_TO_BRACKET_OPTIMUM_IN_LINE_SEARCH() {
		   return new LocalizedFormats("unable to bracket optimum in line search");
		   }
	public static LocalizedFormats UNABLE_TO_COMPUTE_COVARIANCE_SINGULAR_PROBLEM() {
		   return new LocalizedFormats("unable to compute covariances: singular problem");
		   }
	public static LocalizedFormats UNABLE_TO_FIRST_GUESS_HARMONIC_COEFFICIENTS() {
		   return new LocalizedFormats("unable to first guess the harmonic coefficients");
		   }
	public static LocalizedFormats UNABLE_TO_ORTHOGONOLIZE_MATRIX() {
		   return new LocalizedFormats("unable to orthogonalize matrix in {0} iterations");
		   }
	public static LocalizedFormats UNABLE_TO_PERFORM_QR_DECOMPOSITION_ON_JACOBIAN() {
		   return new LocalizedFormats("unable to perform Q.R decomposition on the {0}x{1} jacobian matrix");
		   }
	public static LocalizedFormats UNABLE_TO_SOLVE_SINGULAR_PROBLEM() {
		   return new LocalizedFormats("unable to solve: singular problem");
		   }
	public static LocalizedFormats UNBOUNDED_SOLUTION() {
		   return new LocalizedFormats("unbounded solution");
		   }
	public static LocalizedFormats UNKNOWN_MODE() {
		   return new LocalizedFormats("unknown mode {0}, known modes: {1} ({2}), {3} ({4}), {5} ({6}), {7} ({8}), {9} ({10}) and {11} ({12})");
		   }
	public static LocalizedFormats UNKNOWN_PARAMETER() {
		   return new LocalizedFormats("unknown parameter {0}");
		   }
	public static LocalizedFormats UNMATCHED_ODE_IN_EXPANDED_SET() {
		   return new LocalizedFormats("ode does not match the main ode set in the extended set");
		   }
	public static LocalizedFormats CANNOT_PARSE_AS_TYPE() {
		   return new LocalizedFormats("string \"{0}\" unparseable (from position {1}) as an object of type {2}") /* keep */;
		   }
	public static LocalizedFormats CANNOT_PARSE() {
		   return new LocalizedFormats("string \"{0}\" unparseable (from position {1})") /* keep */;
		   }
	public static LocalizedFormats UNPARSEABLE_3D_VECTOR() {
		   return new LocalizedFormats("unparseable 3D vector: \"{0}\"");
		   }
	public static LocalizedFormats UNPARSEABLE_COMPLEX_NUMBER() {
		   return new LocalizedFormats("unparseable complex number: \"{0}\"");
		   }
	public static LocalizedFormats UNPARSEABLE_REAL_VECTOR() {
		   return new LocalizedFormats("unparseable real vector: \"{0}\"");
		   }
	public static LocalizedFormats UNSUPPORTED_EXPANSION_MODE() {
		   return new LocalizedFormats("unsupported expansion mode {0}, supported modes are {1} ({2}) and {3} ({4})");
		   }
	public static LocalizedFormats UNSUPPORTED_OPERATION() {
		   return new LocalizedFormats("unsupported operation") /* keep */;
		   }
	public static LocalizedFormats ARITHMETIC_EXCEPTION() {
		   return new LocalizedFormats("arithmetic exception") /* keep */;
		   }
	public static LocalizedFormats ILLEGAL_STATE() {
		   return new LocalizedFormats("illegal state") /* keep */;
		   }
	public static LocalizedFormats USER_EXCEPTION() {
		   return new LocalizedFormats("exception generated in user code") /* keep */;
		   }
	public static LocalizedFormats URL_CONTAINS_NO_DATA() {
		   return new LocalizedFormats("URL {0} contains no data");
		   }
	public static LocalizedFormats VALUES_ADDED_BEFORE_CONFIGURING_STATISTIC() {
		   return new LocalizedFormats("{0} values have been added before statistic is configured");
		   }
	public static LocalizedFormats VECTOR_LENGTH_MISMATCH() {
		   return new LocalizedFormats("vector length mismatch: got {0} but expected {1}");
		   }
	public static LocalizedFormats VECTOR_MUST_HAVE_AT_LEAST_ONE_ELEMENT() {
		   return new LocalizedFormats("vector must have at least one element");
		   }
	public static LocalizedFormats WEIGHT_AT_LEAST_ONE_NON_ZERO() {
		   return new LocalizedFormats("weigth array must contain at least one non-zero value");
		   }
	public static LocalizedFormats WRONG_BLOCK_LENGTH() {
		   return new LocalizedFormats("wrong array shape (block length = {0}, expected {1})");
		   }
	public static LocalizedFormats WRONG_NUMBER_OF_POINTS() {
		   return new LocalizedFormats("{0} points are required, got only {1}");
		   }
	public static LocalizedFormats NUMBER_OF_POINTS() {
		   return new LocalizedFormats("number of points ({0})") /* keep */;
		   }
	public static LocalizedFormats ZERO_DENOMINATOR() {
		   return new LocalizedFormats("denominator must be different from 0") /* keep */;
		   }
	public static LocalizedFormats ZERO_DENOMINATOR_IN_FRACTION() {
		   return new LocalizedFormats("zero denominator in fraction {0}/{1}");
		   }
	public static LocalizedFormats ZERO_FRACTION_TO_DIVIDE_BY() {
		   return new LocalizedFormats("the fraction to divide by must not be zero: {0}/{1}");
		   }
	public static LocalizedFormats ZERO_NORM() {
		   return new LocalizedFormats("zero norm");
		   }
	public static LocalizedFormats ZERO_NORM_FOR_ROTATION_AXIS() {
		   return new LocalizedFormats("zero norm for rotation axis");
		   }
	public static LocalizedFormats ZERO_NORM_FOR_ROTATION_DEFINING_VECTOR() {
		   return new LocalizedFormats("zero norm for rotation defining vector");
		   }
	public static LocalizedFormats ZERO_NOT_ALLOWED() {
		   return new LocalizedFormats("zero not allowed here");
		   }

	

    /** Source English format. */
    private final String sourceFormat;

    /** Simple constructor.
     * @param sourceFormat source English format to use when no
     * localized version is available
     */
    LocalizedFormats(final String sourceFormat) {
        this.sourceFormat = sourceFormat;
    }

    /** {@inheritDoc} */
    public String getSourceString() {
        return sourceFormat;
    }

    /** {@inheritDoc} */
    public String getLocalizedString(final Locale locale) {
        try {
            final String path = LocalizedFormats.class.getName().replaceAll("\\.", "/");
            ResourceBundle bundle =
                    ResourceBundle.getBundle("assets/" + path, locale);
            if (bundle.getLocale().getLanguage().equals(locale.getLanguage())) {
                // the value of the resource is the translated format
                return bundle.getString(toString());
            }

        } catch (MissingResourceException mre) { // NOPMD
            // do nothing here
        }

        // either the locale is not supported or the resource is unknown
        // don't translate and fall back to using the source format
        return sourceFormat;

    }

}
