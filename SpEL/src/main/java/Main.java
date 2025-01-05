import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.SimpleEvaluationContext;

/**
 * @Description
 * @Author dragonkeep
 * @Date 2024/12/22
 */
public class Main {
    public static void main(String[] args) throws Exception{
        User user=new User("dragonkeep",20);
        SimpleEvaluationContext context=SimpleEvaluationContext.forReadOnlyDataBinding().build();
        SpelExpressionParser parser =new SpelExpressionParser();
        Expression expression = parser.parseExpression("greet()");
        Object result = expression.getValue(context, user);
        System.out.println(result);
    }
}
