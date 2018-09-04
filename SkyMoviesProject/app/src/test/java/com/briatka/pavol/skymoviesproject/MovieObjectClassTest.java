package com.briatka.pavol.skymoviesproject;

import com.briatka.pavol.skymoviesproject.activities.MainActivity;
import com.briatka.pavol.skymoviesproject.customobjects.MovieObject;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

@RunWith(JUnit4.class)
public class MovieObjectClassTest {

    @Test
    public void public_constructors_exist() {
        Constructor[] constructors = MovieObject.class.getConstructors();
        Assert.assertEquals(2,constructors.length);
        for (Constructor constructor: constructors) {
            Assert.assertTrue("All constructors should be public", Modifier.isPublic(constructor.getModifiers()));
        }
    }

    @Test
    public void first_constructor_string_params_only() {
        Constructor[] constructors = MovieObject.class.getConstructors();
        Constructor firstConstructor = constructors[0];
        Class<?>[] paramTypes = firstConstructor.getParameterTypes();
        for(Class<?> paramType: paramTypes) {
            String type = paramType.getSimpleName();
            Assert.assertTrue("All parameters should be String",type.equals("String"));
        }
    }

    @Test
    public void method_count() {
        Method[] methods = MainActivity.class.getDeclaredMethods();
        Assert.assertEquals(11, methods.length);
    }
}
