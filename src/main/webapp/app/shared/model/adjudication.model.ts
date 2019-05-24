import { IClaim } from 'app/shared/model/claim.model';

export interface IAdjudication {
  id?: number;
  claimnumber?: number;
  casenumber?: number;
  eob?: number;
  claims?: IClaim[];
}

export class Adjudication implements IAdjudication {
  constructor(public id?: number, public claimnumber?: number, public casenumber?: number, public eob?: number, public claims?: IClaim[]) {}
}
