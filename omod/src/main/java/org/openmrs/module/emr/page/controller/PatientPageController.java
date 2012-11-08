/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.emr.page.controller;

import org.openmrs.Patient;
import org.openmrs.api.OrderService;
import org.openmrs.module.emr.EmrContext;
import org.openmrs.module.emr.task.TaskService;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.page.PageModel;
import org.springframework.web.bind.annotation.RequestParam;


/**
 *
 */
public class PatientPageController {

	public void controller(@RequestParam("patientId") Patient patient,
                           EmrContext emrContext,
	                       PageModel model,
                           @SpringBean("orderService") OrderService orderService,
                           @SpringBean("taskService") TaskService taskService) {

        model.addAttribute("patient", patient);
        model.addAttribute("orders", orderService.getOrdersByPatient(patient));
        model.addAttribute("availableTasks", taskService.getAvailableTasks(emrContext));
    }

}
