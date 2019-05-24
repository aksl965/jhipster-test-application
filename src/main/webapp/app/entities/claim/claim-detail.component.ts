import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';

import { IClaim } from 'app/shared/model/claim.model';

@Component({
  selector: 'jhi-claim-detail',
  templateUrl: './claim-detail.component.html'
})
export class ClaimDetailComponent implements OnInit {
  claim: IClaim;

  constructor(protected dataUtils: JhiDataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ claim }) => {
      this.claim = claim;
    });
  }

  byteSize(field) {
    return this.dataUtils.byteSize(field);
  }

  openFile(contentType, field) {
    return this.dataUtils.openFile(contentType, field);
  }
  previousState() {
    window.history.back();
  }
}
