import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { JhipsterTestApplicationSharedModule } from 'app/shared';
import {
  AdjudicationComponent,
  AdjudicationDetailComponent,
  AdjudicationUpdateComponent,
  AdjudicationDeletePopupComponent,
  AdjudicationDeleteDialogComponent,
  adjudicationRoute,
  adjudicationPopupRoute
} from './';

const ENTITY_STATES = [...adjudicationRoute, ...adjudicationPopupRoute];

@NgModule({
  imports: [JhipsterTestApplicationSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    AdjudicationComponent,
    AdjudicationDetailComponent,
    AdjudicationUpdateComponent,
    AdjudicationDeleteDialogComponent,
    AdjudicationDeletePopupComponent
  ],
  entryComponents: [
    AdjudicationComponent,
    AdjudicationUpdateComponent,
    AdjudicationDeleteDialogComponent,
    AdjudicationDeletePopupComponent
  ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class JhipsterTestApplicationAdjudicationModule {}
