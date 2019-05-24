import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { JhipsterTestApplicationSharedModule } from 'app/shared';
import {
  ClaimComponent,
  ClaimDetailComponent,
  ClaimUpdateComponent,
  ClaimDeletePopupComponent,
  ClaimDeleteDialogComponent,
  claimRoute,
  claimPopupRoute
} from './';

const ENTITY_STATES = [...claimRoute, ...claimPopupRoute];

@NgModule({
  imports: [JhipsterTestApplicationSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [ClaimComponent, ClaimDetailComponent, ClaimUpdateComponent, ClaimDeleteDialogComponent, ClaimDeletePopupComponent],
  entryComponents: [ClaimComponent, ClaimUpdateComponent, ClaimDeleteDialogComponent, ClaimDeletePopupComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class JhipsterTestApplicationClaimModule {}
