package com.hundsun.hsccbp.dropwizard.views;

import io.dropwizard.views.View;

import com.hundsun.hsccbp.dropwizard.core.Person;

public class PersonView extends View {
    private final Person person;

    public enum Template {
        FREEMARKER("freemarker/person.ftl"),
        MUSTACHE("mustache/person.mustache");

        private String templateName;

        Template(String templateName) {
            this.templateName = templateName;
        }

        public String getTemplateName() {
            return templateName;
        }
    }

    public PersonView(PersonView.Template template, Person person) {
        super(template.getTemplateName());
        this.person = person;
    }

    public Person getPerson() {
        return person;
    }
}
