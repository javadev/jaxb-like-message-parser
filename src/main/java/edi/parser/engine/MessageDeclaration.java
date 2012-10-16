package edi.parser.engine;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.Documented;

/**
 * Message bean have to be annotated with this annotation
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface MessageDeclaration {
    /**
     * Name of Message, specefic for message type. For example for Edifact.type it can be TKTREQ/TKTRES
     * @return
     */
    String name();
}
