import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'patient',
        loadChildren: './patient/patient.module#JhipsterTestApplicationPatientModule'
      },
      {
        path: 'enrollment',
        loadChildren: './enrollment/enrollment.module#JhipsterTestApplicationEnrollmentModule'
      },
      {
        path: 'claim',
        loadChildren: './claim/claim.module#JhipsterTestApplicationClaimModule'
      },
      {
        path: 'adjudication',
        loadChildren: './adjudication/adjudication.module#JhipsterTestApplicationAdjudicationModule'
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ],
  declarations: [],
  entryComponents: [],
  providers: [],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class JhipsterTestApplicationEntityModule {}
